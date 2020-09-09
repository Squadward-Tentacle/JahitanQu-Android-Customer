package com.example.jahitanqu_customer.data.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.jahitanqu_customer.model.Transaction
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 08/September/2020
 * Email maulibrahim19@gmail.com
 */
class TransactionDataSourceHistoryFactory @Inject constructor(private val transactionDataSourceHistory: TransactionDataSourceHistory):DataSource.Factory<Int,Transaction>() {

    val transactionHistoryLiveDataSource =  MutableLiveData<TransactionDataSourceHistory>()

    override fun create(): DataSource<Int, Transaction> {
        transactionHistoryLiveDataSource.postValue(transactionDataSourceHistory)
        return transactionDataSourceHistory
    }

}