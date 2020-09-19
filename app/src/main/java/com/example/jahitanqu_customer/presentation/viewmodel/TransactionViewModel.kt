package com.example.jahitanqu_customer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.jahitanqu_customer.data.factory.TransactionDataSource
import com.example.jahitanqu_customer.data.factory.TransactionDataSourceFactory
import com.example.jahitanqu_customer.data.factory.TransactionDataSourceHistory
import com.example.jahitanqu_customer.data.factory.TransactionDataSourceHistoryFactory
import com.example.jahitanqu_customer.data.repository.TransactionRepository
import com.example.jahitanqu_customer.model.Address
import com.example.jahitanqu_customer.model.Transaction
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    transactionDataSourceFactory: TransactionDataSourceFactory,
    transactionDataSourceHistoryFactory: TransactionDataSourceHistoryFactory
) : ViewModel() {

    val transaction: LiveData<Transaction>
    val isSuccessPost:LiveData<Boolean>
    val isUpdate:LiveData<Boolean>
    val showShimmerTransactionActive:LiveData<Boolean>
    val showShimmerTransactionHistory:LiveData<Boolean>
    val isEmpty:LiveData<Boolean>
    val isEmptyHistory:LiveData<Boolean>

    val liveDataAddress:MutableLiveData<Address> = MutableLiveData()

    //transaction Active
    val transactionPagedList: LiveData<PagedList<Transaction>>
    private val liveDataSource: LiveData<TransactionDataSource>

    //transaction History
    val transactionHistoryPagedList: LiveData<PagedList<Transaction>>
    private val liveDataSourceHistory: LiveData<TransactionDataSourceHistory>

    init {
        val config = PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(10).build()

        liveDataSource = transactionDataSourceFactory.transactionLiveDataSource
        transactionPagedList = LivePagedListBuilder(transactionDataSourceFactory, config).build()
        showShimmerTransactionActive = transactionDataSourceFactory.showShimmer

        liveDataSourceHistory = transactionDataSourceHistoryFactory.transactionHistoryLiveDataSource
        transactionHistoryPagedList = LivePagedListBuilder(transactionDataSourceHistoryFactory, config).build()
        showShimmerTransactionHistory = transactionDataSourceHistoryFactory.showShimmer

        isEmpty = transactionDataSourceFactory.isEmpty
        isEmptyHistory = transactionDataSourceHistoryFactory.isEmptyHistory

        transaction = transactionRepository.transaction
        isSuccessPost = transactionRepository.isSuccessPost
        isUpdate = transactionRepository.isUpdate
    }

    fun getTransactionById(idTransaction: String) {
        transactionRepository.getTransactionByID(idTransaction)
    }

    fun postTransaction(transaction: Transaction) {
        transactionRepository.postTransaction(transaction)
    }

    fun putTransaction(idTransaction: String,status:String){
        transactionRepository.putTransaction(idTransaction,status)
    }

    fun setAddress(address: Address){
        liveDataAddress.value = address
    }
}