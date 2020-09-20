package com.example.jahitanqu_customer

import android.app.Application
import android.content.Context
import com.example.jahitanqu_customer.common.utils.AccountHelper
import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.di.component.ApplicationComponent
import com.example.jahitanqu_customer.di.component.DaggerApplicationComponent
import com.facebook.FacebookSdk
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import java.net.URISyntaxException


val prefs: AccountHelper by lazy {
    JahitanQu.prefs!!
}

val prefsFcmToken:AccountHelper by lazy {
    JahitanQu.prefsFcmToken!!
}
val signInGoogle:GoogleSignInClient by lazy {
    JahitanQu.googleSignInClient!!
}

val socket:Socket by lazy {
    JahitanQu.socket!!
}

class JahitanQu : Application() {

    val applicationComponent: ApplicationComponent = DaggerApplicationComponent.create()

    override fun onCreate() {
        prefs = AccountHelper(applicationContext)
        prefsFcmToken = AccountHelper(applicationContext)
        initSDKGoogle()
        initSDKFacebook()
        initSDKMidtrans()
        initSocket()
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


    private fun initSocket(){
        try {
            socket = IO.socket(Constant.CHAT_SERVER_URL)
        }catch (e: URISyntaxException){
            e.printStackTrace()
        }
    }

    companion object {
        var prefs: AccountHelper? = null
        var prefsFcmToken :AccountHelper?=null
        var googleSignInClient: GoogleSignInClient? = null
        var socket: Socket? = null
    }

}
