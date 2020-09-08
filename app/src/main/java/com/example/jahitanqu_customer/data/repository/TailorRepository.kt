package com.example.jahitanqu_customer.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.jahitanqu_customer.data.server.apiInterface.TailorApi
import com.example.jahitanqu_customer.model.Tailor
import com.example.jahitanqu_customer.model.Wrapper
import com.example.jahitanqu_customer.prefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 07/September/2020
 * Email maulibrahim19@gmail.com
 */
class TailorRepository @Inject constructor(private val tailorApi: TailorApi) {

    val tailorTopRatedList: MutableLiveData<List<Tailor>> = MutableLiveData()

    fun getTopRatedTailor() {
        tailorApi.getTopRatedTailor(prefs.keyToken!!).enqueue(object : Callback<Wrapper> {
            override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                println(t.localizedMessage)
            }

            override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                if (response.code() == 200) {
                    val response = response.body()
                    val tailor = response?.payload
                    val listOfMyClassObject: Type = object : TypeToken<List<Tailor?>?>() {}.type
                    val gson = Gson()
                    val outputList: List<Tailor> =
                        gson.fromJson(gson.toJson(tailor), listOfMyClassObject)
                    tailorTopRatedList.value = outputList
                }
            }

        })
    }


}