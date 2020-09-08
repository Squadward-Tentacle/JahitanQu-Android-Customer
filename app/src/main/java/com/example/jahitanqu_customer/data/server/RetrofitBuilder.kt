package com.example.jahitanqu_customer.data.server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */

class RetrofitBuilder {

    companion object {
        val BASE_URL = "http://83057bbcdd99.ngrok.io"

        fun createRetrofit(): Retrofit {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}