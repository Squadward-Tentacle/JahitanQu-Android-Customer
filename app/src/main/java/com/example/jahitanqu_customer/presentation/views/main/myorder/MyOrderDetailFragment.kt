package com.example.jahitanqu_customer.presentation.views.main.myorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import cn.pedant.SweetAlert.SweetAlertDialog
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.model.Address
import com.example.jahitanqu_customer.model.Comment
import com.example.jahitanqu_customer.model.FcmToken
import com.example.jahitanqu_customer.model.Transaction
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.viewmodel.AuthViewModel
import com.example.jahitanqu_customer.presentation.viewmodel.TransactionViewModel
import com.example.jahitanqu_customer.presentation.views.maps.MapsActivity
import com.example.jahitanqu_customer.presentation.views.scanner.ScanQrCodeActivity
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.*
import com.midtrans.sdk.corekit.models.snap.Authentication
import com.midtrans.sdk.corekit.models.snap.CreditCard
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_my_order_detail.*
import net.glxn.qrgen.android.QRCode
import java.util.*
import javax.inject.Inject

class MyOrderDetailFragment : Fragment(), View.OnClickListener, TransactionFinishedCallback {

    @Inject
    lateinit var transactionViewModel: TransactionViewModel

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var midtransSDK: MidtransSDK

    lateinit var navController: NavController

    lateinit var sweetAlertDialog: SweetAlertDialog

    lateinit var idTailor: String
    lateinit var idTransaction: String
    var price: Int = 0
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
            generateQRCode(it.idTransaction)
            idTransaction = it.idTransaction
            price = it.cost
            idTailor = it.idTailor
        })

        transactionViewModel.isUpdate.observe(viewLifecycleOwner, Observer {
            if (it) {
                sweetAlertDialog.hide()
                val alertDialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                alertDialog.titleText = getString(R.string.Success)
                alertDialog.show()
                navController.navigate(R.id.toMyOrderFragment)
            }
        })

        authViewModel.isComment.observe(viewLifecycleOwner, Observer {
            if (it) {
                sweetAlertDialog.hide()
                val alertDialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                alertDialog.titleText = getString(R.string.Success)
                alertDialog.show()
            }
        })

    }

    private fun init() {
        midtransSDK.setTransactionFinishedCallback(this)
        btnPayment.setOnClickListener(this)
        btnAddFeedback.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        btnFinishedTransaction.setOnClickListener(this)
    }

    private fun changeStateButton(transaction: Transaction) {
        when (transaction.status) {
            4 -> {
                btnPayment.visibility = View.VISIBLE
                btnAddFeedback.visibility = View.GONE
                btnFinishedTransaction.visibility = View.GONE
            }
            8 -> {
                btnFinishedTransaction.visibility = View.VISIBLE
                btnAddFeedback.visibility = View.GONE
                btnPayment.visibility = View.GONE
            }
            9 -> {
                btnAddFeedback.visibility = View.VISIBLE
                btnPayment.visibility = View.GONE
                btnFinishedTransaction.visibility = View.GONE
            }
            else -> {
                btnPayment.visibility = View.GONE
                btnAddFeedback.visibility = View.GONE
                btnFinishedTransaction.visibility = View.GONE
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
                val commentEditText = dialog.findViewById<EditText>(R.id.etComment)
                val btnSend = dialog.findViewById<Button>(R.id.btnSendComment)
                dialog.show()
                btnSend.setOnClickListener {
                    val comment = Comment(
                        idCustomer = prefs.keyIdCustomer!!,
                        idTailor = idTailor,
                        rating = commentRating.rating.toInt(),
                        comment = commentEditText.text.toString()
                    )
                    authViewModel.comment(comment)
                    dialog.hide()
                    showProgressDialog()
                }
            }
            btnFinishedTransaction -> {
                if (activity?.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(
                        Intent(this.context, ScanQrCodeActivity::class.java),
                        Constant.QRCODE_REQUEST_CODE
                    )
                } else {
                    checkPermission(Manifest.permission.CAMERA)
                }
            }

            btnBack -> {
                navController.navigate(R.id.toMyOrderFragment)
            }
        }
    }

    private fun customerDetail(): CustomerDetails {
        val billingAddress = BillingAddress()
        billingAddress.address = prefs.keySubDistrict
        billingAddress.city = prefs.keyCity
        billingAddress.countryCode = "IDN"
        billingAddress.postalCode = prefs.keyPostalCode
        billingAddress.firstName = prefs.keyFirstname
        billingAddress.lastName = prefs.keyLastName
        billingAddress.phone = prefs.keyPhoneNumber

        val shippingAddress = ShippingAddress()
        shippingAddress.address = prefs.keySubDistrict
        shippingAddress.city = prefs.keyCity
        shippingAddress.countryCode = "IDN"
        shippingAddress.postalCode = prefs.keyPostalCode
        shippingAddress.firstName = prefs.keyFirstname
        shippingAddress.lastName = prefs.keyLastName
        shippingAddress.phone = prefs.keyPhoneNumber

        val customerDetail = CustomerDetails()
        customerDetail.firstName = prefs.keyFirstname
        customerDetail.lastName = prefs.keyLastName
        customerDetail.phone = prefs.keyPhoneNumber
        customerDetail.email = prefs.keyEmail
        customerDetail.billingAddress = billingAddress
        customerDetail.shippingAddress = shippingAddress

        return customerDetail
    }

    private fun transactionRequest(
        id: String,
        price: Int,
        qty: Int,
        name: String
    ): TransactionRequest {

        val detail = ItemDetails(id, price.toDouble(), qty, name)
        val itemDetails = mutableListOf<ItemDetails>()
        itemDetails.add(detail)

        val request = TransactionRequest(id, price.toDouble())
        request.itemDetails = itemDetails as ArrayList<ItemDetails>?
        request.customerDetails = customerDetail()


        val creditCard = CreditCard()
        creditCard.isSaveCard = false
        creditCard.authentication = Authentication.AUTH_RBA
        creditCard.bank = BankType.MANDIRI
        request.creditCard = creditCard
        return request
    }

    override fun onTransactionFinished(p0: TransactionResult?) {
        if (p0?.response != null) {
            when (p0.status) {
                TransactionResult.STATUS_SUCCESS -> {
                    Toast.makeText(
                        this.context,
                        "Transaction Finished:${p0.response.transactionId}",
                        Toast.LENGTH_SHORT
                    ).show()
                    transactionViewModel.putTransaction(idTransaction, "5")
                }
                TransactionResult.STATUS_PENDING -> {
                    Toast.makeText(
                        this.context,
                        "Transaction Pending: ${p0.response.transactionId}",
                        Toast.LENGTH_SHORT
                    ).show()
                    transactionViewModel.putTransaction(idTransaction, "5")
                }
                TransactionResult.STATUS_FAILED -> {
                    Toast.makeText(
                        this.context,
                        "Transaction Pending: ${p0.response.transactionId}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            p0.response.validationMessages

        } else if (p0?.isTransactionCanceled!!) {
            Toast.makeText(
                this.context,
                "Transaction Canceled: ${p0.response.transactionId}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (p0.status == TransactionResult.STATUS_INVALID) {
                Toast.makeText(this.context, "Transaction Invalid", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this.context,
                    "Transaction Finished with failure",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun showProgressDialog() {
        sweetAlertDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.progressHelper.barColor = resources.getColor(R.color.colorDarkBrown);
        sweetAlertDialog.titleText = getString(R.string.progressbar_loading)
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.show()
    }

    private fun generateQRCode(contain: String) {
        val myBitmap: Bitmap = QRCode.from(contain).withSize(150, 150).bitmap()
        ivQrCode.setImageBitmap(myBitmap)
    }

    private fun checkPermission(permission: String) {
        when (permission) {
            Manifest.permission.CAMERA -> {
                if (activity?.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA
                        ),
                        Constant.REQUEST_READ_CAMERA_PERMISSION
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
        if (requestCode == Constant.REQUEST_READ_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(
                    Intent(this.context, ScanQrCodeActivity::class.java),
                    Constant.QRCODE_REQUEST_CODE
                )
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.QRCODE_REQUEST_CODE) {
            if (data != null) {
                val transactionId = data!!.getStringExtra(Constant.KEY_ID_TRANSACTION)
                if (transactionId == idTransaction) {
                    showProgressDialog()
                    transactionViewModel.putTransaction(idTransaction, "9")
                    authViewModel.pushNotification(
                        idTailor,
                        FcmToken(message = "Transaction Finished")
                    )
                }

            }

        }
    }

}
