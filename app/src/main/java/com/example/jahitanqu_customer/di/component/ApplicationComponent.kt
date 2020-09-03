package com.example.jahitanqu_customer.di.component

import com.example.jahitanqu_customer.di.module.ApplicationModule
import com.example.jahitanqu_customer.di.module.FirebaseModule
import com.example.jahitanqu_customer.di.module.NetworkModule
import com.example.jahitanqu_customer.views.authentication.AuthActivity
import com.example.jahitanqu_customer.views.authentication.fragment.LoginFragment
import com.example.jahitanqu_customer.views.authentication.fragment.RegisterFragment
import dagger.Component

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
@Component(modules = [FirebaseModule::class,NetworkModule::class,ApplicationModule::class])
interface ApplicationComponent {
    fun inject(authActivity: AuthActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(registerFragment: RegisterFragment)
}