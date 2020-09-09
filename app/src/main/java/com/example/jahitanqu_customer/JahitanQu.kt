package com.example.jahitanqu_customer

import android.app.Application
import com.example.jahitanqu_customer.common.utils.AccountHelper
import com.example.jahitanqu_customer.di.component.ApplicationComponent
import com.example.jahitanqu_customer.di.component.DaggerApplicationComponent
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.uikit.SdkUIFlowBuilder


val prefs: AccountHelper by lazy {
    JahitanQu.prefs!!
}

val signInGoogle:GoogleSignInClient by lazy {
    JahitanQu.googleSignInClient!!
}

class JahitanQu : Application() {

    val applicationComponent: ApplicationComponent = DaggerApplicationComponent.create()

    override fun onCreate() {
        prefs = AccountHelper(applicationContext)
        initSDKGoogle()
        initSDKFacebook()
        initSDKMidtrans()
        super.onCreate()
    }

    private fun initSDKGoogle() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun initSDKFacebook(){
        FacebookSdk.sdkInitialize(this.applicationContext);
    }

    private fun initSDKMidtrans(){
        SdkUIFlowBuilder.init()
            .setClientKey(BuildConfig.MERCHANT_CLIENT_KEY) // client_key is mandatory
            .setContext(this.applicationContext)
            .setTransactionFinishedCallback {
            }
            .setMerchantBaseUrl(BuildConfig.MERCHANT_BASE_URL)
            .enableLog(true)
            .setColorTheme(
                CustomColorTheme(
                    "#FFE51255",
                    "#B61548",
                    "#FFE51255"
                )
            )
            .buildSDK()
    }

    companion object {
        var prefs: AccountHelper? = null
        var googleSignInClient: GoogleSignInClient? = null
    }


}
