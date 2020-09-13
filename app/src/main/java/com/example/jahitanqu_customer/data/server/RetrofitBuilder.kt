package com.example.jahitanqu_customer.data.server

import com.example.jahitanqu_customer.common.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */

class RetrofitBuilder {

    companion object {
        fun createRetrofit(): Retrofit {
            return Retrofit.Builder().baseUrl(Constant.BACKEND_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}