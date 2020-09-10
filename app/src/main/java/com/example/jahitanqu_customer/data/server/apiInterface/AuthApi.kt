package com.example.jahitanqu_customer.data.server.apiInterface

import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.data.server.ApiEndPoint.ENDPOINT_COMMENT_CUSTOMER
import com.example.jahitanqu_customer.data.server.ApiEndPoint.ENDPOINT_GET_TOKEN
import com.example.jahitanqu_customer.data.server.ApiEndPoint.ENDPOINT_LOGIN_CUSTOMER
import com.example.jahitanqu_customer.data.server.ApiEndPoint.ENDPOINT_REGISTER_CUSTOMER
import com.example.jahitanqu_customer.data.server.ApiEndPoint.ENDPOINT_UPDATE_CUSTOMER
import com.example.jahitanqu_customer.model.Comment
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.model.Wrapper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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

    @Multipart
    @PUT(ENDPOINT_UPDATE_CUSTOMER)
    fun updateCustomer(
        @Header(Constant.KEY_AUTHORIZATION) auth:String,
        @Path("idCustomer") idCustomer:String,
        @Part(Constant.KEY_EMAIL) email:RequestBody,
        @Part(Constant.KEY_FIRSTNAME) firstname:RequestBody,
        @Part(Constant.KEY_LASTNAME) lastname:RequestBody,
        @Part(Constant.KEY_ADDRESSNAME) addressName:RequestBody,
        @Part(Constant.KEY_LATITUDE) latitude:RequestBody,
        @Part(Constant.KEY_LONGITUDE) longitude:RequestBody,
        @Part(Constant.KEY_LONGITUDE) phone:RequestBody,
        @Part avatarImageUrl:MultipartBody.Part
    ):Call<Wrapper>

    @POST(ENDPOINT_COMMENT_CUSTOMER)
    fun comment(
        @Header(Constant.KEY_AUTHORIZATION) auth: String,
        @Body comment: Comment
    ):Call<Wrapper>
}