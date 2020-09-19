package com.example.jahitanqu_customer.data.factory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.jahitanqu_customer.model.Transaction
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 08/September/2020
 * Email maulibrahim19@gmail.com
 */
class TransactionDataSourceFactory @Inject constructor(private val transactionDataSource: TransactionDataSource):DataSource.Factory<Int,Transaction>() {

    val transactionLiveDataSource =  MutableLiveData<TransactionDataSource>()
    val showShimmer:LiveData<Boolean> = transactionDataSource.showShimmer
    val isEmpty:LiveData<Boolean> = transactionDataSource.isEmpty

    override fun create(): DataSource<Int, Transaction> {
        transactionLiveDataSource.postValue(transactionDataSource)
        return transactionDataSource
    }

}