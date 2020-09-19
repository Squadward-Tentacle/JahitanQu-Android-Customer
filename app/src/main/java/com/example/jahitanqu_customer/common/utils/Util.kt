package com.example.jahitanqu_customer.common.utils

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.jahitanqu_customer.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
object Util {

    fun convertStatusToString(status:Int):String{
        return when(status){
            1 -> "Waiting Confirmation"
            2 -> "Confirmed"
            3 -> "Waiting Payment"
            4 -> "Payment Confirmed"
            5 -> "On Process"
            6 -> "Done"
            else -> ""
        }
    }

    fun convertMultipartFile(file: File,name:String): MultipartBody.Part{
        return MultipartBody.Part.createFormData(
            name,
            file.name,
            RequestBody.create("multipart".toMediaTypeOrNull(), file)
        )
    }

    fun convertRequestBody(value:String):RequestBody{
        return RequestBody.create("text/plain".toMediaTypeOrNull(),value)
    }

    fun validationInput(vararg inputs:String):Boolean{
        for (input in inputs){
            if (input.isNullOrEmpty()){
                return false
            }
        }
        return true
    }

    fun showAlert(context:Context,type:Int,title:String,content:String){
        SweetAlertDialog(context, type)
            .setTitleText(title)
            .setContentText(content)
            .show()
    }
}