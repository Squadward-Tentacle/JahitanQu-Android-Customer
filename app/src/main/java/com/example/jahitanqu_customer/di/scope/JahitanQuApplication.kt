package com.example.jahitanqu_customer.di.scope

import android.app.Application
import com.example.jahitanqu_customer.di.component.ApplicationComponent
import com.example.jahitanqu_customer.di.component.DaggerApplicationComponent

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */

class JahitanQuApplication:Application() {
    val applicationComponent: ApplicationComponent = DaggerApplicationComponent.create()
}