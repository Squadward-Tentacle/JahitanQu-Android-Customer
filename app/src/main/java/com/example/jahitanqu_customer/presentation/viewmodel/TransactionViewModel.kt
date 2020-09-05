package com.example.jahitanqu_customer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.jahitanqu_customer.data.repository.TransactionRepository
import com.example.jahitanqu_customer.model.Transaction
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
class TransactionViewModel @Inject constructor(private val transactionRepository: TransactionRepository):ViewModel() {

    val transactionList:LiveData<List<Transaction>>

    init {
        transactionList = transactionRepository.transactionList
    }

    fun getTransaction(){
        transactionRepository.getTransaction()
    }

}