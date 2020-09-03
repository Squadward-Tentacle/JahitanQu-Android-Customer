package com.example.jahitanqu_customer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.views.authentication.AuthRepository
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository):ViewModel() {

    fun login(customer: Customer){
        authRepository.login(customer)
    }

    fun getToken(){
        authRepository.getToken()
    }
}