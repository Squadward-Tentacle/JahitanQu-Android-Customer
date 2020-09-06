package com.example.jahitanqu_customer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.data.repository.AuthRepository
import java.io.File
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    val isLogin: LiveData<Boolean>
    val isRegister: LiveData<Boolean>
    val isUpdated :LiveData<Boolean>

    init {
        isLogin = authRepository.isLogin
        isRegister = authRepository.isRegister
        isUpdated = authRepository.isUpdated
    }

    fun login(customer: Customer) {
        authRepository.login(customer)
    }

    fun getToken() {
        authRepository.getToken()
    }

    fun register(customer: Customer) {
        authRepository.register(customer)
    }

    fun updateCustomer(customer: Customer,avatarImageUrl: File){
        authRepository.updateCustomer(customer,avatarImageUrl)
    }
}