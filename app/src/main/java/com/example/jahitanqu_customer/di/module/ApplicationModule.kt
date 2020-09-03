package com.example.jahitanqu_customer.di.module

import com.example.jahitanqu_customer.presentation.views.authentication.AuthContract
import com.example.jahitanqu_customer.presentation.views.authentication.fragment.LoginFragment
import com.example.jahitanqu_customer.presentation.views.authentication.fragment.RegisterFragment
import dagger.Module
import dagger.Provides

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */

@Module
class ApplicationModule {

    @Provides
    fun provideAuthContractLogin():AuthContract.login = LoginFragment()

    @Provides
    fun provideAuthContractRegister():AuthContract.register = RegisterFragment()

}