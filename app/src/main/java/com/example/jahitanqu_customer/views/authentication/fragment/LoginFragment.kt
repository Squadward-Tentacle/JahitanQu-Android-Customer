package com.example.jahitanqu_customer.views.authentication.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jahitanqu_customer.JahitanQu

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.signInGoogle
import com.example.jahitanqu_customer.viewmodel.AuthViewModel
import com.example.jahitanqu_customer.views.authentication.AuthContract
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment(),View.OnClickListener,AuthContract.login {

    lateinit var navController: NavController
    @Inject
    lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var authViewModel: AuthViewModel

    private val RC_SIGN_IN: Int = 21


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JahitanQu).applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
    }

    private fun init() {
        handleFacebookCallback()
        btnRegisterNow.setOnClickListener(this)
        btnLoginFb.setOnClickListener(this)
        btnLoginGoogle.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
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
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val user = firebaseAuth.currentUser
                prefs.keyEmail = user?.email
                authViewModel.getToken()
            }else{
                Log.w(
                    getString(R.string.facebook_auth),
                    getString(R.string.sign_in_with_credential_filed),
                    it.exception
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
                authViewModel.getToken()
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
            btnRegisterNow ->{
                navController.navigate(R.id.toRegisterFragment)
            }
            btnLogin -> {
                val customer = Customer(
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString()
                )
                authViewModel.login(customer)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs.keyEmail != "" || prefs.keyEmail != null) {
            println(prefs.keyEmail)
        }
    }

    override fun onSuccess() {
        println("LOGIN SUCCESS")
    }

    override fun onFailure() {
        println("LOGIN SUCCESS")
    }

}
