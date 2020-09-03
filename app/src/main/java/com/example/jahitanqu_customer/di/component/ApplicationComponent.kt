package com.example.jahitanqu_customer.di.component

import com.example.jahitanqu_customer.MainActivity
import com.example.jahitanqu_customer.di.module.FirebaseModule
import com.example.jahitanqu_customer.view.activity.AuthActivity
import dagger.Component

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
@Component(modules = [FirebaseModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(authActivity: AuthActivity)
}