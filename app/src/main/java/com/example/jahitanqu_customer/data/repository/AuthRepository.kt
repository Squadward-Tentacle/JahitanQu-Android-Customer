package com.example.jahitanqu_customer.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.jahitanqu_customer.data.server.apiInterface.AuthApi
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.model.Wrapper
import com.example.jahitanqu_customer.prefs
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) {

    val isLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRegister: MutableLiveData<Boolean> = MutableLiveData(false)

    fun login(customer: Customer) {
        authApi.login(customer).enqueue(object : Callback<Wrapper> {
            override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                println(t.localizedMessage)
            }

            override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                val responseCustomer = response.body()
                if (responseCustomer?.statusCode == 200) {
                    val res = responseCustomer?.payload
                    val gson = Gson()
                    val customer = gson.fromJson(
                        gson.toJson(res),
                        Customer::class.java
                    )
                    prefs.keyToken = responseCustomer.token
                    isLogin.value = true
                } else {
                    isLogin.value = false
                }
            }

        })
    }

    fun getToken() {
        authApi.getToken().enqueue(object : Callback<Wrapper> {
            override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                println(t.localizedMessage)
            }

            override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                val responseCustomer = response.body()
                if (responseCustomer?.statusCode == 200) {
                    val res = responseCustomer?.payload
                    val gson = Gson()
                    val customer = gson.fromJson(
                        gson.toJson(res),
                        Customer::class.java
                    )
                    prefs.keyToken = responseCustomer?.token
                    isLogin.value = true
                } else {
                    isLogin.value = false
                }
            }

        })
    }

    fun register(customer: Customer) {
        authApi.register(customer).enqueue(object : Callback<Wrapper> {
            override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                println(t.localizedMessage)
            }

            override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                val responseCustomer = response.body()
                if (responseCustomer?.statusCode == 201) {
                    val res = responseCustomer?.payload
                    val gson = Gson()
                    val customer = gson.fromJson(
                        gson.toJson(res),
                        Customer::class.java
                    )
                    prefs.keyToken = responseCustomer?.token
                    isRegister.value = true
                } else {
                    isRegister.value = false
                }
            }

        })
    }
}