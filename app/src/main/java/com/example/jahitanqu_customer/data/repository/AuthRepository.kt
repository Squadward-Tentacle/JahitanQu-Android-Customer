package com.example.jahitanqu_customer.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.jahitanqu_customer.common.utils.Util
import com.example.jahitanqu_customer.data.server.apiInterface.AuthApi
import com.example.jahitanqu_customer.model.Comment
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.model.Wrapper
import com.example.jahitanqu_customer.prefs
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import java.io.File
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
    val isUpdated: MutableLiveData<Boolean> = MutableLiveData(false)
    val isComment: MutableLiveData<Boolean> = MutableLiveData(false)


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
                    prefs.keyIdCustomer = customer.idCustomer
                    prefs.keyEmail = customer.email
                    prefs.keyFirstname = customer.firstname
                    prefs.keyLastName = customer.lastname
                    prefs.keyAddressName = customer.address.addressName
                    prefs.keyLatitude = customer.address.latitude
                    prefs.keyLongitude = customer.address.longitude
                    prefs.keyPhoneNumber = customer.phone
                    prefs.keyPhotoUrl = customer.imageUrl
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
                    prefs.keyIdCustomer = customer.idCustomer
                    prefs.keyEmail = customer.email
                    prefs.keyFirstname = customer.firstname
                    prefs.keyLastName = customer.lastname
                    prefs.keyToken = responseCustomer?.token
                    isRegister.value = true
                } else {
                    isRegister.value = false
                }
            }

        })
    }

    fun updateCustomer(customer: Customer, image: File) {
        val token = prefs.keyToken
        val avatarImageUrl = Util.convertMultipartFile(image, "avatarImageUrl")
        val email = Util.convertRequestBody(customer.email)
        val fname = Util.convertRequestBody(customer.firstname)
        val lname = Util.convertRequestBody(customer.lastname)
        val addressName =Util.convertRequestBody(customer.address.addressName)
        val lat = Util.convertRequestBody(customer.address.latitude.toString())
        val lng = Util.convertRequestBody(customer.address.longitude.toString())
        val phone = Util.convertRequestBody(customer.phone)
        authApi.updateCustomer(
            token!!,
            prefs.keyIdCustomer!!,
            email,
            fname,
            lname,
            addressName,
            lat,
            lng,
            phone,
            avatarImageUrl
        ).enqueue(object : Callback<Wrapper> {
            override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                println(t.localizedMessage)
            }

            override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                val responseCustomer = response.body()
                if (responseCustomer?.statusCode == 200) {
                    val res = responseCustomer?.payload
                    prefs.setCustomerPref(res)
                    val gson = Gson()
                    val customer = gson.fromJson(
                        gson.toJson(res),
                        Customer::class.java
                    )
                    prefs.keyIdCustomer = customer.idCustomer
                    prefs.keyEmail = customer.email
                    prefs.keyFirstname = customer.firstname
                    prefs.keyLastName = customer.lastname
                    prefs.keyAddressName = customer.address.addressName
                    prefs.keyLatitude = customer.address.latitude
                    prefs.keyLongitude = customer.address.longitude
                    prefs.keyPhoneNumber = customer.phone
                    prefs.keyPhotoUrl = customer.imageUrl
                    prefs.keyToken = responseCustomer?.token
                    isUpdated.value = true
                } else {
                    isUpdated.value = false
                }
            }

        })

    }

    fun comment(comment: Comment){
        authApi.comment(prefs.keyToken!!,comment).enqueue(object:Callback<Wrapper>{
            override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                println(t.localizedMessage)
            }

            override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                isComment.value = response.code() == 200
            }

        })
    }
}