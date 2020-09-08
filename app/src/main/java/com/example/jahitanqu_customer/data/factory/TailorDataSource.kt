package com.example.jahitanqu_customer.data.factory

import androidx.paging.PageKeyedDataSource
import com.example.jahitanqu_customer.data.repository.TailorRepository
import com.example.jahitanqu_customer.data.server.apiInterface.TailorApi
import com.example.jahitanqu_customer.model.Tailor
import com.example.jahitanqu_customer.model.Wrapper
import com.example.jahitanqu_customer.prefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 07/September/2020
 * Email maulibrahim19@gmail.com
 */
class TailorDataSource @Inject constructor(private val tailorApi: TailorApi) :
    PageKeyedDataSource<Int, Tailor>() {

    private val FIRST_PAGE = 1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Tailor>
    ) {
        tailorApi.getTailor(prefs.keyToken!!, FIRST_PAGE).enqueue(object : Callback<Wrapper> {
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
                    callback.onResult(outputList, null, FIRST_PAGE + 1)
                }
            }

        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Tailor>) {
        tailorApi.getTailor(prefs.keyToken!!, params.key).enqueue(object : Callback<Wrapper> {
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
                    callback.onResult(outputList, params.key + 1)
                }
            }

        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Tailor>) {
        var key: Int = 0
        if (params.key > 1) {
            key = params.key - 1
        }
        tailorApi.getTailor(prefs.keyToken!!, params.key).enqueue(object : Callback<Wrapper> {
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
                    callback.onResult(outputList, key)

                }
            }

        })

    }
}