package com.example.jahitanqu_customer.data.server.apiInterface

import com.example.jahitanqu_customer.data.server.ApiEndPoint.ENDPOINT_GET_TOKEN
import com.example.jahitanqu_customer.data.server.ApiEndPoint.ENDPOINT_LOGIN_CUSTOMER
import com.example.jahitanqu_customer.data.server.ApiEndPoint.ENDPOINT_REGISTER_CUSTOMER
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.model.Wrapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
interface AuthApi {

    @POST(ENDPOINT_LOGIN_CUSTOMER)
    fun login(@Body customer: Customer):Call<Wrapper>

    @GET(ENDPOINT_GET_TOKEN)
    fun getToken():Call<Wrapper>

    @POST(ENDPOINT_REGISTER_CUSTOMER)
    fun register(@Body customer: Customer):Call<Wrapper>

}