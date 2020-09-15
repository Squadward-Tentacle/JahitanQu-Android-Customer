package com.example.jahitanqu_customer.presentation.views.authentication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.jahitanqu_customer.JahitanQu

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.utils.Util
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.model.FcmToken
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.etEmail
import kotlinx.android.synthetic.main.fragment_register.etPassword
import javax.inject.Inject

class RegisterFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var authViewModel: AuthViewModel
    lateinit var navController: NavController
    lateinit var sweetAlertDialog: SweetAlertDialog

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
                if (!prefs.keyCustomerFcm.isNullOrEmpty()) {
                    val fcmToken = FcmToken(
                        tokenId = prefs.keyIdCustomer!!,
                        token = prefs.keyCustomerFcm!!
                    )
                    authViewModel.postFcm(fcmToken)
                }
                sweetAlertDialog.hide()
                navController.navigate(R.id.toHomeActivity)
            } else {
                sweetAlertDialog.hide()
                SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getString(R.string.opps))
                    .setContentText(getString(R.string.registration_failed))
                    .show()
            }
        })
        btnRegister.setOnClickListener(this)
        btnLoginNow.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnRegister -> {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val firstname = etFirstName.text.toString()
                val lastname = etLastName.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()
                if (Util.validationInput(email, firstname, password, lastname, confirmPassword)) {
                    val customer = Customer(
                        email = email,
                        password = password,
                        firstname = firstname,
                        lastname = lastname
                    )
                    showProgressDialog()
                    authViewModel.register(customer)
                } else {
                    SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.opps))
                        .setContentText(getString(R.string.please_fill_in_all_fields))
                        .show()
                }
            }
            btnLoginNow -> {
                navController.navigate(R.id.toLoginFragment)
            }
        }
    }

    private fun showProgressDialog() {
        sweetAlertDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.progressHelper.barColor = resources.getColor(R.color.colorDarkBrown);
        sweetAlertDialog.titleText = getString(R.string.progressbar_loading)
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.show()
    }


}
