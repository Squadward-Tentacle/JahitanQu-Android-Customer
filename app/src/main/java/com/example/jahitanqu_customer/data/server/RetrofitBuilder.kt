package com.example.jahitanqu_customer.data.server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */

class RetrofitBuilder {

    companion object {
        val BASE_URL = "http://5fd24c7241a6.ngrok.io"

        fun createRetrofit(): Retrofit {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}