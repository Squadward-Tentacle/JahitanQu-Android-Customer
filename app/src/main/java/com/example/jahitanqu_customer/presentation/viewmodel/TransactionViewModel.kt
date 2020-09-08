package com.example.jahitanqu_customer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.jahitanqu_customer.data.factory.TransactionDataSource
import com.example.jahitanqu_customer.data.factory.TransactionDataSourceFactory
import com.example.jahitanqu_customer.data.repository.TransactionRepository
import com.example.jahitanqu_customer.model.Transaction
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    transactionDataSourceFactory: TransactionDataSourceFactory
) : ViewModel() {

    val transaction: LiveData<Transaction>
    val isSuccessPost:LiveData<Boolean>

    val transactionPagedList: LiveData<PagedList<Transaction>>
    private val liveDataSource: LiveData<TransactionDataSource>

    init {
        liveDataSource = transactionDataSourceFactory.transactionLiveDataSource
        val config = PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(6).build()
        transactionPagedList = LivePagedListBuilder(transactionDataSourceFactory, config).build()
        transaction = transactionRepository.transaction
        isSuccessPost = transactionRepository.isSuccessPost
    }

    fun getTransactionById(idTransaction: String) {
        transactionRepository.getTransactionByID(idTransaction)
    }

    fun postTransaction(transaction: Transaction) {
        transactionRepository.postTransaction(transaction)
    }
}