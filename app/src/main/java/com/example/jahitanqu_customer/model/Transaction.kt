package com.example.jahitanqu_customer.model

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
data class Transaction(
  val idTransaction:String,
  val customer: Customer,
  val tailor:Tailor,
  val address: Address,
  val status:Int,
  val cost:Int,
  val imageUrl:String,
  val description:String,
  val transactionDate:String
)