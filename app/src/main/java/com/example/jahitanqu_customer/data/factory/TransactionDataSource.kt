package com.example.jahitanqu_customer.data.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.jahitanqu_customer.data.repository.TransactionRepository
import com.example.jahitanqu_customer.data.server.apiInterface.TransactionApi
import com.example.jahitanqu_customer.model.Transaction
import com.example.jahitanqu_customer.model.Wrapper
import com.example.jahitanqu_customer.prefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 08/September/2020
 * Email maulibrahim19@gmail.com
 */
class TransactionDataSource @Inject constructor(private val transactionApi: TransactionApi) :
    PageKeyedDataSource<Int, Transaction>() {
    private val FIRST_PAGE = 1

    val showShimmer = MutableLiveData(true)
    val isEmpty = MutableLiveData(true)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Transaction>
    ) {
        transactionApi.getTransactionActive(prefs.keyToken!!, prefs.keyIdCustomer!!, FIRST_PAGE)
            .enqueue(object : Callback<Wrapper> {
                override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                    println(t.localizedMessage)
                }

                override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                    val responseTransaction = response.body()
                    val res = responseTransaction?.payload
                    val listOfMyClassObject: Type =
                        object : TypeToken<List<Transaction?>?>() {}.type
                    val gson = Gson()
                    val outputList: List<Transaction> =
                        gson.fromJson(gson.toJson(res), listOfMyClassObject)
                    callback.onResult(outputList, null, FIRST_PAGE + 1)
                    showShimmer.value = false
                    isEmpty.value = outputList.isEmpty()
                }

            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Transaction>) {
        transactionApi.getTransactionActive(prefs.keyToken!!, prefs.keyIdCustomer!!, params.key)
            .enqueue(object : Callback<Wrapper> {
                override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                    println(t.localizedMessage)
                }

                override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                    val responseTransaction = response.body()
                    val res = responseTransaction?.payload
                    val listOfMyClassObject: Type =
                        object : TypeToken<List<Transaction?>?>() {}.type
                    val gson = Gson()
                    val outputList: List<Transaction> =
                        gson.fromJson(gson.toJson(res), listOfMyClassObject)
                    if (params.key <= responseTransaction?.metaData?.pageCount!!) {
                        callback.onResult(outputList, params.key + 1)
                    }
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Transaction>) {

        transactionApi.getTransactionActive(prefs.keyToken!!, prefs.keyIdCustomer!!, params.key)
            .enqueue(object : Callback<Wrapper> {
                override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                    println(t.localizedMessage)
                }

                override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                    val responseTransaction = response.body()
                    val res = responseTransaction?.payload
                    val listOfMyClassObject: Type =
                        object : TypeToken<List<Transaction?>?>() {}.type
                    val gson = Gson()
                    val outputList: List<Transaction> =
                        gson.fromJson(gson.toJson(res), listOfMyClassObject)
                    if (params.key > 1) {
                        callback.onResult(outputList, params.key - 1)
                    }
                }
            })

    }
}