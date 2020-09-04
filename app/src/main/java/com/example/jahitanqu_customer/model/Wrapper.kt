package com.example.jahitanqu_customer.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
class Wrapper (
    @SerializedName("Status")
    val statusCode:Int,
    @SerializedName("Message")
    val message:String,
    @SerializedName("Payload")
    val payload:Any,
    @SerializedName("Token")
    var token:String
)