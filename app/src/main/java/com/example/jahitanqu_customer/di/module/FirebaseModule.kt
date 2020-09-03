package com.example.jahitanqu_customer.di.module

import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */

@Module
class FirebaseModule {
    @Provides
    fun provideFirebaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideCallbackManager():CallbackManager{
        return CallbackManager.Factory.create()
    }
}