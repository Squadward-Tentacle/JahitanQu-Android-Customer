package com.example.jahitanqu_customer.view.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.jahitanqu_customer.R
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(),View.OnClickListener {

    lateinit var navController: NavController
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN:Int = 21
    val TAG: String = "facebookAuthentication"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        btnRegisterNow.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0){
            btnRegisterNow ->{
                navController.navigate(R.id.toRegisterFragment)
            }
        }
    }

}
