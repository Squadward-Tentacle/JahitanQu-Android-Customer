package com.example.jahitanqu_customer.presentation.views.main.myorder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.jahitanqu_customer.JahitanQu

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.utils.Util
import com.example.jahitanqu_customer.model.Transaction
import com.example.jahitanqu_customer.presentation.viewmodel.TransactionViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_my_order_detail.*
import javax.inject.Inject

class MyOrderDetailFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var transactionViewModel: TransactionViewModel

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JahitanQu).applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idTransaction = arguments?.getString("idTransaction")
        transactionViewModel.getTransactionById(idTransaction!!)
        pbDetailOrder.visibility = View.VISIBLE
        transactionViewModel.transaction.observe(viewLifecycleOwner, Observer {
            pbDetailOrder.visibility = View.GONE
            changeStateButton(it)
            tvTransactionStatus.text = it.statusName
            tvTransactionNumber.text = it.idTransaction
            tvTailorName.text = it.firstnameTailor
            tvTransactionDesc.text = it.description
            tvTransactionAddress.text = it.address.addressName
            if (!it.imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(it.imageUrl)
                    .into(ivTransactionImage)
            }
            tvTransactionCost.text = "Rp. ${it.cost}"
        })
        navController = Navigation.findNavController(view)
        btnPayment.setOnClickListener(this)
        btnAddFeedback.setOnClickListener(this)
        btnBack.setOnClickListener(this)
    }

    private fun changeStateButton(transaction: Transaction){
        when(transaction.status){
            2 -> {
                btnPayment.visibility = View.VISIBLE
                btnAddFeedback.visibility = View.GONE
            }
            8 -> {
                btnAddFeedback.visibility = View.VISIBLE
                btnPayment.visibility = View.GONE
            }
            else ->{
                btnPayment.visibility = View.VISIBLE
                btnPayment.isEnabled = false
                btnAddFeedback.visibility = View.GONE
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnPayment -> {

            }
            btnAddFeedback -> {
                val dialog = MaterialDialog(activity?.window!!.context)
                    .noAutoDismiss()
                    .customView(R.layout.feedback_popup)
                dialog.show()
            }
            btnBack -> {
                navController.navigate(R.id.toMyOrderFragment)
            }
        }
    }
}
