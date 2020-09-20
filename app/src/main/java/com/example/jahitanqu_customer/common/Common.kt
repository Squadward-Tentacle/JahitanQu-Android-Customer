package com.example.jahitanqu_customer.common

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.jahitanqu_customer.R

/**
 * Created by Maulana Ibrahim on 20/September/2020
 * Email maulibrahim19@gmail.com
 */
object Common {

    var sweetAlertDialog: SweetAlertDialog? = null


    fun showProgressDialog(context: Context) {
        dismissProgressDialog()
        sweetAlertDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.apply {
            this!!.titleText = "Loading"
            progressHelper.barColor = context.getColor(R.color.colorDarkBrown)
            setConfirmClickListener(null)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
        if (context is Activity && !context.isFinishing) sweetAlertDialog!!.show()
    }

    fun dismissProgressDialog() {
        if (sweetAlertDialog != null && sweetAlertDialog!!.isShowing) {
            sweetAlertDialog!!.dismiss()
            sweetAlertDialog = null
        }
    }

    fun showPopUpDialog(
        context: Context,
        title: String,
        message: String,
        alertType: Int = SweetAlertDialog.SUCCESS_TYPE
    ) {
        val dialog = SweetAlertDialog(context, alertType)
        dialog.apply {
            titleText = title
            contentText = message
            confirmText = "OK"
            setConfirmClickListener(null)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
        dialog.show()
    }

    fun showPopUpDialogWaring(
        context: Context,
        title: String,
        message: String,
        alertType: Int = SweetAlertDialog.WARNING_TYPE
    ) {
        val dialog = SweetAlertDialog(context, alertType)
        dialog.apply {
            titleText = title
            contentText = message
            confirmText = "OK"
            setConfirmClickListener(null)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
        dialog.show()
    }

}