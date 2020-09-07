package com.example.jahitanqu_customer.data.server.apiInterface

import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.data.server.ApiEndPoint
import com.example.jahitanqu_customer.model.Wrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Created by Maulana Ibrahim on 07/September/2020
 * Email maulibrahim19@gmail.com
 */
interface TailorApi {

    @GET(ApiEndPoint.ENDPOINT_GET_TOP_RATED_TAILOR)
    fun getTopRatedTailor(
        @Header(Constant.KEY_AUTHORIZATION) auth:String
    ): Call<Wrapper>

    @GET(ApiEndPoint.ENDPOINT_GET_TAILOR)
    fun getTailor(
        @Header(Constant.KEY_AUTHORIZATION) auth:String,
        @Path("page") page:Int
    ): Call<Wrapper>
}