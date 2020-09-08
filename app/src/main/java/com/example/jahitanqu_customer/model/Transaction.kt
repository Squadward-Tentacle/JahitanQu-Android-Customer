package com.example.jahitanqu_customer.model

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
data class Transaction(
  val idTransaction:String="",
  val idTailor: String="",
  val firstnameTailor:String="",
  val idCustomer:String="",
  val firstnameCustomer:String="",
  val status:Int=0,
  val statusName:String="",
  val address: Address=Address(),
  val cost:Int=0,
  val description:String="",
  val imageUrl:String="",
  val transactionDate:String=""
)