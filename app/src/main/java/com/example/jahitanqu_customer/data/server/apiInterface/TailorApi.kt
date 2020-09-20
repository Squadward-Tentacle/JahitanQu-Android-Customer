package com.example.jahitanqu_customer.data.server.apiInterface

import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.data.server.ApiEndPoint
import com.example.jahitanqu_customer.model.Address
import com.example.jahitanqu_customer.model.Wrapper
import retrofit2.Call
import retrofit2.http.*

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
        @Path(Constant.KEY_PAGE) page:Int
    ): Call<Wrapper>

    @GET(ApiEndPoint.ENDPOINT_GET_TAILOR_BY_ID)
    fun getTailorById(
        @Header(Constant.KEY_AUTHORIZATION) auth: String,
        @Path(Constant.KEY_ID_TAILOR) idTailor:String
    ):Call<Wrapper>

    @POST(ApiEndPoint.ENDPOINT_GET_NEARBY_TAILOR)
    fun getNearbyTailor(
        @Header(Constant.KEY_AUTHORIZATION) auth:String,
        @Body address: Address
    ):Call<Wrapper>
}