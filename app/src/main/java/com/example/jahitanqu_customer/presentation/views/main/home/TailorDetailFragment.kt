package com.example.jahitanqu_customer.presentation.views.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jahitanqu_customer.JahitanQu

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.model.Address
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.model.Tailor
import com.example.jahitanqu_customer.model.Transaction
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.viewmodel.TailorViewModel
import com.example.jahitanqu_customer.presentation.viewmodel.TransactionViewModel
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleCommentAdapter
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecyclePortofolioAdapter
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleTailorAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_tailor_detail.*
import kotlinx.android.synthetic.main.fragment_tailor_list.*
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
        idTailor = arguments?.getString("idTailor")!!
        pbLoading.visibility = View.VISIBLE
        btnReservation.setOnClickListener(this)
        rvRatingAndReview.layoutManager = LinearLayoutManager(context)
        rvPortofolio.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        tailorViewModel.getTailorById(idTailor!!)
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

        navController = Navigation.findNavController(view)
        transactionViewModel.isSuccessPost.observe(viewLifecycleOwner, Observer {
            if (it){
                navController.navigate(R.id.toMyOrderFragment)
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnReservation -> {
                val transaction = Transaction(
                    idCustomer = prefs.keyIdCustomer!!,
                    idTailor = idTailor,
                    address = Address(
                        "Jl.Ayam goreng", (-6.7891).toFloat(), 6.789F, 1
                    ),
                    status = 1
                )
                transactionViewModel.postTransaction(transaction)
            }
        }
    }
}
