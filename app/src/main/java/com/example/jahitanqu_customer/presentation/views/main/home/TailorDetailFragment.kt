package com.example.jahitanqu_customer.presentation.views.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.model.Address
import com.example.jahitanqu_customer.model.Transaction
import com.example.jahitanqu_customer.prefs
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
    lateinit var transactionViewModel: TransactionViewModel

    lateinit var recycleCommentAdapter: RecycleCommentAdapter

    lateinit var recyclePortofolioAdapter: RecyclePortofolioAdapter

    lateinit var navController: NavController

    lateinit var idTailor: String

    lateinit var address: Address

    val REQUEST_CODE_MAPS = 101

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
        observSuccessPost()
    }

    private fun init() {
        idTailor = arguments?.getString("idTailor")!!
        pbLoading.visibility = View.VISIBLE
        btnReservation.setOnClickListener(this)
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

    private fun observSuccessPost() {
        transactionViewModel.isSuccessPost.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(activity,"Success", Toast.LENGTH_LONG).show()

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
            recycleCommentAdapter = RecycleCommentAdapter(it.comment)
            rvRatingAndReview.adapter = recycleCommentAdapter
            recyclePortofolioAdapter = RecyclePortofolioAdapter(it.portofolio)
            rvPortofolio.adapter = recyclePortofolioAdapter
        })

    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnReservation -> {
                val dialog = MaterialDialog(activity?.window!!.context, BottomSheet())
                    .cornerRadius(16f)
                    .noAutoDismiss()
                    .customView(R.layout.reservation_popup)
                    .setPeekHeight(450)
                val addressEditText = dialog.findViewById<EditText>(R.id.etAddressBooking)
                val booking = dialog.findViewById<Button>(R.id.btnBookin)
                val btnSetPlace = dialog.findViewById<Button>(R.id.btnSetPlace)

                transactionViewModel.liveDataAddress.observe(viewLifecycleOwner, Observer {
                    addressEditText.setText(it.addressName)
                })

                dialog.show()

                booking.setOnClickListener {
                    val transaction = Transaction(
                        idCustomer = prefs.keyIdCustomer!!,
                        idTailor = idTailor,
                        address = address,
                        status = 1
                    )
                    transactionViewModel.postTransaction(transaction)
                    dialog.hide()
                }

                btnSetPlace.setOnClickListener {
                    startActivityForResult(Intent(this.context, MapsActivity::class.java) ,REQUEST_CODE_MAPS)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MAPS) {
            val latitude = data!!.getDoubleExtra("LATITUDE",0.0)
            val longitude = data.getDoubleExtra("LONGITUDE",0.0)
            val addresses = data.getStringExtra("ADDRESS")
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
