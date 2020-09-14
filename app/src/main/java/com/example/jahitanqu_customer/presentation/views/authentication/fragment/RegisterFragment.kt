package com.example.jahitanqu_customer.presentation.views.authentication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.jahitanqu_customer.JahitanQu

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.model.FcmToken
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject

class RegisterFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var authViewModel: AuthViewModel
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JahitanQu).applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        authViewModel.isRegister.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (!prefs.keyCustomerFcm.isNullOrEmpty()){
                    val fcmToken = FcmToken(
                        tokenId = prefs.keyIdCustomer!!,
                        token = prefs.keyCustomerFcm!!
                    )
                    authViewModel.postFcm(fcmToken)
                }
                navController.navigate(R.id.toHomeActivity)
            }
        })
        btnRegister.setOnClickListener(this)
        btnLoginNow.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnRegister -> {
                val customer = Customer(
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString(),
                    firstname = etFirstName.text.toString(),
                    lastname = etLastName.text.toString()
                )
                authViewModel.register(customer)
            }
            btnLoginNow -> {
                navController.navigate(R.id.toLoginFragment)
            }
        }
    }


}
