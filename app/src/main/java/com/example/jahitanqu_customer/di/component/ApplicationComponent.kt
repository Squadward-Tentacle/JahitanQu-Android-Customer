package com.example.jahitanqu_customer.di.component

import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.di.module.FirebaseModule
import com.example.jahitanqu_customer.view.authentication.AuthActivity
import dagger.Component

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
@Component(modules = [FirebaseModule::class])
interface ApplicationComponent {
    fun inject(authActivity: AuthActivity)
}