package com.example.jahitanqu_customer.model

import java.util.*

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
data class Customer(
    var idCustomer:String = "",
    var email:String = "",
    var password:String = "",
    var firstname:String = "",
    var lastname:String = "",
    var imageUrl:String = "",
    var phone:String = "",
    var address: Address = Address(),
    var createdAt:String = "",
    var udpatedAt:String = "",
    var deletedAt:String = ""
)