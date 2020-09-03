package com.example.jahitanqu_customer.di.module

import com.example.jahitanqu_customer.views.authentication.AuthContract
import com.example.jahitanqu_customer.views.authentication.fragment.LoginFragment
import dagger.Module
import dagger.Provides

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */

@Module
class ApplicationModule {

    @Provides
    fun provideBaseContract():AuthContract = LoginFragment()

}