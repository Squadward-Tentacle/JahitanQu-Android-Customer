package com.example.jahitanqu_customer.presentation.views.main.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.Common
import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.model.Address
import com.example.jahitanqu_customer.model.FcmToken
import com.example.jahitanqu_customer.model.Transaction
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.viewmodel.AuthViewModel
import com.example.jahitanqu_customer.presentation.viewmodel.TailorViewModel
import com.example.jahitanqu_customer.presentation.viewmodel.TransactionViewModel
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleCommentAdapter
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecyclePortofolioAdapter
import com.example.jahitanqu_customer.presentation.views.maps.MapsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_tailor_detail.*
import javax.inject.Inject


class TailorDetailFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var tailorViewModel: TailorViewModel

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var transactionViewModel: TransactionViewModel

    lateinit var recycleCommentAdapter: RecycleCommentAdapter

    lateinit var recyclePortofolioAdapter: RecyclePortofolioAdapter

    lateinit var navController: NavController

    lateinit var idTailor: String

    lateinit var address: Address

    var latitude: Float = 0.0f

    var longitude: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JahitanQu).applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tailor_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        navController = Navigation.findNavController(view)
        tailorViewModel.getTailorById(idTailor!!)
        observeTailor()
        observeSuccessPost()
    }

    private fun init() {
        idTailor = arguments?.getString(Constant.KEY_ID_TAILOR)!!
        pbLoading.visibility = View.VISIBLE
        btnReservation.setOnClickListener(this)
        btnContact.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        btnToLocation.setOnClickListener(this)
        rvRatingAndReview.layoutManager = LinearLayoutManager(context)
        rvPortofolio.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        address = Address(
            prefs.keyAddressName!!,
            prefs.keyLatitude!!,
            prefs.keyLongitude!!
        )
        transactionViewModel.setAddress(address)

    }

    private fun observeSuccessPost() {
        transactionViewModel.isSuccessPost.observe(viewLifecycleOwner, Observer {
            if (it) {
                Common.dismissProgressDialog()
                Common.showPopUpDialog(
                    requireContext(),
                    getString(R.string.success),
                    getString(R.string.transaction_success),
                    SweetAlertDialog.SUCCESS_TYPE
                )
                authViewModel.pushNotification(idTailor, FcmToken(message = "Waiting Confirmation"))
                navController.navigate(R.id.toMyOrderFragment)
            }
        })
    }

    private fun observeTailor() {
        tailorViewModel.tailor.observe(viewLifecycleOwner, Observer {
            pbLoading.visibility = View.GONE
            if (it.imageUrl.isNotEmpty()) {
                Picasso.get()
                    .load(it.imageUrl)
                    .placeholder(R.drawable.ic_photo)
                    .error(R.drawable.ic_photo)
                    .into(ivTailor)
            }
            tvTailorName.text = "${it.firstname} ${it.lastname}"
            tvTailorAddress.text = it.address.addressName
            rbRatingTailor.rating = it.rating.toFloat()
            tvTailorDescription.text = it.description
            latitude = it.address.latitude
            longitude = it.address.longitude
            recycleCommentAdapter = RecycleCommentAdapter(it.comment)
            rvRatingAndReview.adapter = recycleCommentAdapter
            recyclePortofolioAdapter = RecyclePortofolioAdapter(it.portofolio)
            rvPortofolio.adapter = recyclePortofolioAdapter
        })

    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnReservation -> {
                if (prefs.keyIdCustomer.isNullOrEmpty()) {
                    Common.showPopUpDialog(
                        requireContext(),
                        getString(R.string.opps),
                        getString(R.string.not_found_id_customer),
                        SweetAlertDialog.WARNING_TYPE
                    )
                } else {
                    val dialog = MaterialDialog(activity?.window!!.context, BottomSheet())
                        .cornerRadius(16f)
                        .noAutoDismiss()
                        .customView(R.layout.reservation_popup)
                        .setPeekHeight(450)
                    val addressEditText = dialog.findViewById<EditText>(R.id.etAddressBooking)
                    val booking = dialog.findViewById<Button>(R.id.btnBookin)
                    val btnSetPlace = dialog.findViewById<Button>(R.id.btnSetPlace)
                    dialog.show()

                    transactionViewModel.liveDataAddress.observe(viewLifecycleOwner, Observer {
                        addressEditText.setText(it.addressName)
                    })

                    booking.setOnClickListener {
                        dialog.hide()
                        Common.showProgressDialog(this.requireContext())
                        postTransaction()
                    }
                    btnSetPlace.setOnClickListener {
                        openMaps()
                    }
                }
            }
            btnContact -> {
                val bundle = bundleOf(Constant.KEY_ID_TAILOR to idTailor)
                navController.navigate(R.id.chatFragment, bundle)
            }

            btnToLocation -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=${latitude},${longitude}")
                )
                startActivity(intent)
            }

            btnBack -> {
                activity?.onBackPressed()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.REQUEST_CODE_MAPS) {
            if (data != null) {
                val latitude = data!!.getDoubleExtra(Constant.KEY_LATITUDE, 0.0)
                val longitude = data.getDoubleExtra(Constant.KEY_LONGITUDE, 0.0)
                val addresses = data.getStringExtra(Constant.KEY_ADDRESS)
                address = Address(
                    addresses,
                    latitude.toFloat(),
                    longitude.toFloat(),
                    2
                )
                transactionViewModel.setAddress(address)

            }
        }
    }

    private fun checkPermission(permission: String) {
        when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                if (activity?.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        Constant.REQUEST_READ_FINE_LOCATION_PERMISSION
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.REQUEST_READ_FINE_LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(
                    Intent(this.context, MapsActivity::class.java),
                    Constant.REQUEST_CODE_MAPS
                )
            } else {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.location_denied))
                    .show()
            }
        }
    }

    private fun openMaps() {
        if (activity?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(
                Intent(this.context, MapsActivity::class.java),
                Constant.REQUEST_CODE_MAPS
            )
        } else {
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun postTransaction() {
        val transaction = Transaction(
            idCustomer = prefs.keyIdCustomer!!,
            idTailor = idTailor,
            address = address,
            status = 1
        )
        transactionViewModel.postTransaction(transaction)
    }

}
