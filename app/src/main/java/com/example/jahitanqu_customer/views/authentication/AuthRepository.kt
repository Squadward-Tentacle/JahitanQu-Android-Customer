package com.example.jahitanqu_customer.views.authentication

import com.example.jahitanqu_customer.data.server.AuthApi
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
    private val authApi: AuthApi,
    private val authContractLogin: AuthContract.login,
    private val authContractRegister:AuthContract.register
) {

    fun login(customer: Customer) {
        authApi.login(customer).enqueue(object : Callback<Wrapper> {
            override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                println(t.localizedMessage)
            }

            override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                if (response.code() == 200) {
                    val responseCustomer = response.body()
                    val res = responseCustomer?.payload
                    val gson = Gson()
                    val customer = gson.fromJson(
                        gson.toJson(res),
                        Customer::class.java
                    )
                    authContractLogin.onSuccess()
                } else {
                    authContractLogin.onFailure()
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
                if (response.code() == 200) {
                    val responseCustomer = response.body()
                    val res = responseCustomer?.payload
                    val gson = Gson()
                    val customer = gson.fromJson(
                        gson.toJson(res),
                        Customer::class.java
                    )
                    prefs.keyToken = customer.token
                    authContractLogin.onSuccess()
                } else {
                    authContractLogin.onFailure()
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
                if (response.code() == 200) {
                    val responseCustomer = response.body()
                    val res = responseCustomer?.payload
                    val gson = Gson()
                    val customer = gson.fromJson(
                        gson.toJson(res),
                        Customer::class.java
                    )
                    prefs.keyToken = customer.token
                    authContractRegister.onSuccess()
                } else {
                    authContractRegister.onFailure()
                }
            }

        })
    }
}