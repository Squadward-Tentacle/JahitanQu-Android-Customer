package com.example.jahitanqu_customer.presentation.views.main.myorder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.jahitanqu_customer.JahitanQu

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.common.utils.Util
import com.example.jahitanqu_customer.model.Comment
import com.example.jahitanqu_customer.model.Transaction
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.viewmodel.AuthViewModel
import com.example.jahitanqu_customer.presentation.viewmodel.TransactionViewModel
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.BankType
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.snap.Authentication
import com.midtrans.sdk.corekit.models.snap.CreditCard
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_my_order_detail.*
import java.util.ArrayList
import javax.inject.Inject

class MyOrderDetailFragment : Fragment(), View.OnClickListener,TransactionFinishedCallback {

    @Inject
    lateinit var transactionViewModel: TransactionViewModel

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject lateinit var midtransSDK: MidtransSDK

    lateinit var navController: NavController

    lateinit var idTailor: String

    private lateinit var idTransaction:String

    var price:Int = 0

    lateinit var name: String


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
        init()
        navController = Navigation.findNavController(view)
        idTransaction = arguments?.getString(Constant.KEY_ID_TRANSACTION)!!
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
            idTransaction = it.idTransaction
            price = it.cost
            idTailor = it.idTailor
        })

        transactionViewModel.isUpdate.observe(viewLifecycleOwner, Observer {
            if (it){
                navController.navigate(R.id.toMyOrderFragment)
            }
        })

        authViewModel.isComment.observe(viewLifecycleOwner, Observer {
            if (it){
                Toast.makeText(activity,"Success", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun init(){
        midtransSDK.setTransactionFinishedCallback(this)
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
                btnAddFeedback.visibility = View.GONE
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnPayment -> {
                midtransSDK.transactionRequest = transactionRequest(
                    idTransaction,
                    price,
                    1,
                    "Service"
                )
                midtransSDK.startPaymentUiFlow(this.context)
            }
            btnAddFeedback -> {
                val dialog = MaterialDialog(activity?.window!!.context)
                    .noAutoDismiss()
                    .customView(R.layout.feedback_popup)
                val commentRating = dialog.findViewById<RatingBar>(R.id.rbComment)
                val commentEditText = dialog.findViewById<Button>(R.id.etComment)
                val btnSend = dialog.findViewById<Button>(R.id.btnSendComment)
                dialog.show()
                val comment = Comment(
                    idCustomer = prefs.keyIdCustomer!!,
                    idTailor = idTailor,
                    rating = commentRating.numStars,
                    comment = commentEditText.text.toString()
                )
                btnSend.setOnClickListener {
                    authViewModel.comment(comment)
                    dialog.hide()
                }
            }
            btnBack -> {
                navController.navigate(R.id.toMyOrderFragment)
            }
        }
    }

    private fun customerDetail():CustomerDetails{
        val customerDetail = CustomerDetails()
        customerDetail.firstName = prefs.keyFirstname
        customerDetail.phone = prefs.keyPhoneNumber
        customerDetail.email = prefs.keyEmail
        return customerDetail
    }

    private fun transactionRequest(id:String, price:Int, qty:Int, name:String):TransactionRequest{
        val request = TransactionRequest("${System.currentTimeMillis()} ", price.toDouble())
        request.customerDetails= customerDetail()
        val detail = ItemDetails(id, price.toDouble(),qty,name)
        val itemDetails = mutableListOf<ItemDetails>()
        itemDetails.add(detail)
        request.itemDetails = itemDetails as ArrayList<ItemDetails>?

        val creditCard = CreditCard()
        creditCard.isSaveCard = false
        creditCard.authentication = Authentication.AUTH_RBA
        creditCard.bank = BankType.MANDIRI
        request.creditCard = creditCard
        return request
    }

    override fun onTransactionFinished(p0: TransactionResult?) {
        if (p0?.response != null){
            when(p0.status){
                TransactionResult.STATUS_SUCCESS->{
                    Toast.makeText(this.context,"Transaction Finished:${p0.response.transactionId}",Toast.LENGTH_SHORT).show()
                }
                TransactionResult.STATUS_PENDING ->{
                    Toast.makeText(this.context,"Transaction Pending: ${p0.response.transactionId}",Toast.LENGTH_SHORT).show()
                }
                TransactionResult.STATUS_FAILED ->{
                    Toast.makeText(this.context,"Transaction Pending: ${p0.response.transactionId}",Toast.LENGTH_SHORT).show()
                }
            }
            p0.response.validationMessages

        }else if (p0?.isTransactionCanceled!!){
            Toast.makeText(this.context,"Transaction Canceled: ${p0.response.transactionId}",Toast.LENGTH_SHORT).show()
        }else{
            if (p0.status == TransactionResult.STATUS_INVALID){
                Toast.makeText(this.context,"Transaction Invalid",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this.context,"Transaction Finished with failure",Toast.LENGTH_SHORT).show()
            }
        }
    }


}
