package com.example.jahitanqu_customer.data.server.apiInterface

import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.data.server.ApiEndPoint
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.model.Transaction
import com.example.jahitanqu_customer.model.Wrapper
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
interface TransactionApi {

    @GET(ApiEndPoint.ENDPOINT_GET_TRANSACTION)
    fun getTransaction(
        @Header(Constant.KEY_AUTHORIZATION) token: String,
        @Path(Constant.KEY_ID_CUSTOMER) idCustomer: String,
        @Path(Constant.KEY_PAGE) page: Int
    ): Call<Wrapper>

    @GET(ApiEndPoint.ENDPOINT_GET_TRANSACTION_BY_ID)
    fun getTransactionById(
        @Header(Constant.KEY_AUTHORIZATION) token:String,
        @Path(Constant.KEY_ID_TRANSACTION) idTransaction: String
    ): Call<Wrapper>

    @POST(ApiEndPoint.ENDPOINT_POST_TRANSACTION)
    fun postTransaction(
        @Header(Constant.KEY_AUTHORIZATION) token:String,
        @Body transaction: Transaction
    ):Call<Wrapper>

}