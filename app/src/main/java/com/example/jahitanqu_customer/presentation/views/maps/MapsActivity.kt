package com.example.jahitanqu_customer.presentation.views.maps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jahitanqu_customer.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.*
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import kotlinx.android.synthetic.main.map_content.*
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var predictionList: List<AutocompletePrediction>
    private lateinit var mLastKnownLocation: Location
    private lateinit var locationCallback: LocationCallback
    lateinit var placeClient: PlacesClient
    lateinit var mapView: View
    private val DEFAULT_ZOOM = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapView = mapFragment.requireView()
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
        placeClient = Places.createClient(this)
        val token = AutocompleteSessionToken.newInstance();

        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {}
            override fun onSearchConfirmed(text: CharSequence) {
                startSearch(text.toString(), true, null, true)
            }

            override fun onButtonClicked(buttonCode: Int) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
//                    searchBar.disableSearch()
                }
            }
        })

        searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                val predictionsRequest: FindAutocompletePredictionsRequest =
                    FindAutocompletePredictionsRequest.builder()
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build()
                placeClient.findAutocompletePredictions(predictionsRequest)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val predictionsResponse: FindAutocompletePredictionsResponse? =
                                task.result
                            if (predictionsResponse != null) {
                                predictionList =
                                    predictionsResponse.autocompletePredictions
                                val suggestionsList = mutableListOf<String>()
                                for (i in predictionList.indices) {
                                    val prediction = predictionList[i]
                                    suggestionsList.add(prediction.getFullText(null).toString())
                                }
                                searchBar.updateLastSuggestions(suggestionsList)
                                if (!searchBar.isSuggestionsVisible) {
                                    searchBar.showSuggestionsList()
                                }
                            }
                        } else {
                            Log.i("mytag", "prediction fetching task unsuccessful")
                        }
                    }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchBar.setSuggestionsClickListener(object : SuggestionsAdapter.OnItemViewClickListener {
            override fun OnItemClickListener(position: Int, v: View?) {
                if (position >= predictionList.size) {
                    return
                }
                val selectedPrediction = predictionList[position]
                val suggestion: String = searchBar.lastSuggestions[position].toString()
                searchBar.text = suggestion
                Handler().postDelayed(Runnable { searchBar.clearSuggestions() }, 1000)
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)?.hideSoftInputFromWindow(
                    searchBar.windowToken,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
                val placeId = selectedPrediction.placeId
                val placeFields: List<Place.Field> = listOf(Place.Field.LAT_LNG)
                val fetchPlaceRequest: FetchPlaceRequest =
                    FetchPlaceRequest.builder(placeId, placeFields).build()
                placeClient.fetchPlace(fetchPlaceRequest)
                    .addOnSuccessListener { p0 ->
                        val place: Place = p0?.place!!
                        Log.i("mytag", "Place found: " + place.name)
                        val latLngOfPlace: LatLng? = place.latLng
                        if (latLngOfPlace != null) {
                            mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    latLngOfPlace,
                                    DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    }.addOnFailureListener { e ->
                        if (e is ApiException) {
                            val apiException: ApiException = e as ApiException
                            apiException.printStackTrace()
                            val statusCode: Int = apiException.statusCode
                            Log.i("mytag", "place not found: " + e.message)
                            Log.i("mytag", "status code: $statusCode")
                        }
                    }
            }

            override fun OnItemDeleteListener(position: Int, v: View?) {}
        })
        btn_find.setOnClickListener {
            val currentMarkerLocation = mMap.cameraPosition.target
            val address = getAddress(currentMarkerLocation.latitude,currentMarkerLocation.longitude)
            val intent = Intent()
            intent.putExtra("LATITUDE", currentMarkerLocation.latitude)
            intent.putExtra("LONGITUDE", currentMarkerLocation.longitude)
            intent.putExtra("ADDRESS",address)
            setResult(101, intent)
            finish()
        }

    }

    private fun getAddress(lat: Double, lng: Double): String {
        var geocoder = Geocoder(this, Locale.getDefault())
        var addresses = geocoder.getFromLocation(lat, lng, 1)
        val address: String = addresses[0].getAddressLine(0)
        val city: String = addresses[0].locality
        val state: String = addresses[0].adminArea
        val country: String = addresses[0].countryName
        val postalCode: String = addresses[0].postalCode
        return "${address},${city},${state},${country},${postalCode}"
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        if (mapView.findViewById<View?>("1".toInt()) != null) {
            val locationButton =
                (mapView.findViewById<View>("1".toInt())
                    .parent as View).findViewById<View>("2".toInt())
            val layoutParams =
                locationButton.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 40, 180)
        }

        //check if gps is enabled or not and then request user to enable it
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> =
            settingsClient.checkLocationSettings(builder.build())
        task.addOnSuccessListener(
            this
        ) { getDeviceLocation() }
        task.addOnFailureListener(this) { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this, 51)
                } catch (e1: SendIntentException) {
                    e1.printStackTrace()
                }
            }
        }
        mMap.setOnMyLocationButtonClickListener {
            if (searchBar.isSuggestionsVisible) {
                searchBar.clearSuggestions()
            }
            false
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 51) {
            if (resultCode == Activity.RESULT_OK) {
                getDeviceLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        mFusedLocationProviderClient.lastLocation
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mLastKnownLocation = task.result!!
                    if (mLastKnownLocation != null) {
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()
                                ), DEFAULT_ZOOM.toFloat()
                            )
                        )
                    } else {
                        val locationRequest = LocationRequest.create()
                        locationRequest.interval = 10000
                        locationRequest.fastestInterval = 5000
                        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult?) {
                                super.onLocationResult(locationResult)
                                if (locationResult == null) {
                                    return
                                }
                                mLastKnownLocation = locationResult.lastLocation
                                mMap.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            mLastKnownLocation.latitude,
                                            mLastKnownLocation.longitude
                                        ), DEFAULT_ZOOM.toFloat()
                                    )
                                )
                                mFusedLocationProviderClient.removeLocationUpdates(locationCallback)
                            }
                        }
                        mFusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            null
                        )
                    }

                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "unable to get last location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
