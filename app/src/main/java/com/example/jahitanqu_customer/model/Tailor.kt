package com.example.jahitanqu_customer.model

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
data class Tailor(
    val idTailor: String,
    val email: String,
    val firstname: String,
    val lastname: String,
    val phone: String,
    val description:String,
    val imageUrl: String = "",
    val rating:Int = 0,
    val address: Address = Address(),
    val portofolio: List<Portofolio>,
    val comment: List<Comment>,
    val createdAt: String = "",
    val udpatedAt: String = "",
    val deletedAt: String = ""
)