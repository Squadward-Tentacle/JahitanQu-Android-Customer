package com.example.jahitanqu_customer

import android.app.Application
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jahitanqu_customer.common.utils.AccountHelper
import com.example.jahitanqu_customer.di.component.ApplicationComponent
import com.example.jahitanqu_customer.di.component.DaggerApplicationComponent
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

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

    companion object {
        var prefs: AccountHelper? = null
        var googleSignInClient: GoogleSignInClient? = null
    }


}
