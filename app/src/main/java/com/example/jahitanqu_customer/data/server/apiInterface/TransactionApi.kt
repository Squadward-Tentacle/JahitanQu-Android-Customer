package com.example.jahitanqu_customer.data.server.apiInterface

import com.example.jahitanqu_customer.data.server.ApiEndPoint
import com.example.jahitanqu_customer.model.Wrapper
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
interface TransactionApi {

    @GET(ApiEndPoint.ENDPOINT_GET_TRANSACTION)
    fun getTransaction(): Call<Wrapper>

}