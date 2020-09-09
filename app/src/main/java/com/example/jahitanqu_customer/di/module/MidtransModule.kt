package com.example.jahitanqu_customer.di.module

import com.midtrans.sdk.corekit.core.MidtransSDK
import dagger.Module
import dagger.Provides

/**
 * Created by Maulana Ibrahim on 09/September/2020
 * Email maulibrahim19@gmail.com
 */
@Module
class MidtransModule {

    @Provides
    fun provideMidtransSdk():MidtransSDK{
        return MidtransSDK.getInstance()
    }

}