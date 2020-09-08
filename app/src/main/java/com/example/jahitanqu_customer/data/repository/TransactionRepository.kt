package com.example.jahitanqu_customer.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.jahitanqu_customer.data.server.apiInterface.TransactionApi
import com.example.jahitanqu_customer.model.Transaction
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
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
class TransactionRepository @Inject constructor(private val transactionApi: TransactionApi) {

    val transaction: MutableLiveData<Transaction> = MutableLiveData()
    val isSuccessPost: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getTransactionByID(idTransaction: String) {
        transactionApi.getTransactionById(prefs.keyToken!!, idTransaction)
            .enqueue(object : Callback<Wrapper> {
                override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                    println(t.localizedMessage)
                }

                override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                    val responseTransaction = response.body()
                    val res = responseTransaction?.payload
                    val listOfMyClassObject: Type = object : TypeToken<Transaction?>() {}.type
                    val gson = Gson()
                    val outputList: Transaction =
                        gson.fromJson(gson.toJson(res), listOfMyClassObject)
                    transaction.value = outputList
                }
            })
    }

    fun postTransaction(transaction: Transaction) {
        transactionApi.postTransaction(prefs.keyToken!!, transaction)
            .enqueue(object : Callback<Wrapper> {
                override fun onFailure(call: Call<Wrapper>, t: Throwable) {
                    println(t.localizedMessage)
                }

                override fun onResponse(call: Call<Wrapper>, response: Response<Wrapper>) {
                    val responseTransaction = response.body()
                    val res = responseTransaction?.statusCode
                    isSuccessPost.value = res == 201
                }

            })
    }

}