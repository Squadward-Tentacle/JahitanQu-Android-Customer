package com.example.jahitanqu_customer.common.utils

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
object util {

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

}