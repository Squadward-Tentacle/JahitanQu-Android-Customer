package com.example.jahitanqu_customer.view.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.signInGoogle
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class AuthActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val RC_SIGN_IN: Int = 21


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        (applicationContext as JahitanQu).applicationComponent.inject(this)
        init()
    }

    private fun init() {
        handleFacebookCallback()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                handleGoogleLogin(account.idToken)
            } catch (e: ApiException) {
                Log.w(
                    getString(R.string.facebook_auth),
                    getString(R.string.google_sign_in_filed),
                    e
                )
            }
        }
    }

    private fun handleFacebookCallback() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    handleFacebookLogin(result?.accessToken)
                }

                override fun onCancel() {
                    Log.d(getString(R.string.facebook_auth), getString(R.string.onCancel))
                }

                override fun onError(error: FacebookException?) {
                    Log.d(getString(R.string.facebook_auth), getString(R.string.onEroor))
                }
            })

    }

    private fun handleGoogleLogin(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    prefs.keyEmail = user?.email
                } else {
                    Log.w(
                        getString(R.string.facebook_auth),
                        getString(R.string.sign_in_with_credential_filed),
                        task.exception
                    )
                }
            }
    }

    private fun handleFacebookLogin(accessToken: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(accessToken?.token!!)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser
                prefs.keyEmail = user?.email
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnLoginGoogle -> {
                val signInIntent: Intent = signInGoogle.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
            btnLoginFb -> {
                LoginManager.getInstance()
                    .logInWithReadPermissions(this, listOf("email", "public_profile"))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs.keyEmail != "" || prefs.keyEmail != null) {
            println(prefs.keyEmail)
        }
    }
}
