package com.example.jahitanqu_customer.data.server

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
object ApiEndPoint {
    const val ENDPOINT_LOGIN_CUSTOMER = "customer/login"
    const val ENDPOINT_GET_TOKEN = "customer/token"
    const val ENDPOINT_REGISTER_CUSTOMER = "customer/register"
    const val ENDPOINT_GET_TRANSACTION = "transaction/{idCustomer}"
    const val ENDPOINT_UPDATE_CUSTOMER = "customer/{idCustomer}"
    const val ENDPOINT_GET_TRANSACTION_BY_ID = "transaction/{idTransaction}"
    const val ENDPOINT_GET_TOP_RATED_TAILOR = "tailor/topRate"
    const val ENDPOINT_GET_TAILOR = "tailor/{page}"
}