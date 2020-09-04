package com.example.jahitanqu_customer.di.component

import com.example.jahitanqu_customer.di.module.FirebaseModule
import com.example.jahitanqu_customer.di.module.NetworkModule
import com.example.jahitanqu_customer.presentation.views.authentication.AuthActivity
import com.example.jahitanqu_customer.presentation.views.authentication.fragment.LoginFragment
import com.example.jahitanqu_customer.presentation.views.authentication.fragment.RegisterFragment
import com.example.jahitanqu_customer.presentation.views.home.HomeActivity
import com.example.jahitanqu_customer.presentation.views.home.fragment.HomeFragment
import dagger.Component

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
@Component(modules = [FirebaseModule::class, NetworkModule::class])
interface ApplicationComponent {
    fun inject(authActivity: AuthActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(registerFragment: RegisterFragment)
    fun inject(homeActivity: HomeActivity)
    fun inject(homeFragment: HomeFragment)
}