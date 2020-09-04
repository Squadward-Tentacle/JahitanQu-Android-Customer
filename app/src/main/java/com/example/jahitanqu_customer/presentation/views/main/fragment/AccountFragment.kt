package com.example.jahitanqu_customer.presentation.views.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.prefs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etFirstname.setText(prefs.keyFirstname)
        etEmail.setText(prefs.keyEmail)
        etPhoneNumber.setText(prefs.keyPhoneNumber)
        Picasso.get()
            .load(prefs.keyPhotoUrl)
            .into(profile_image)
        btnEdit.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0){
            btnEdit ->{
                btnEdit.visibility = View.GONE
                btnSave.visibility = View.VISIBLE
                etFirstname.isEnabled = true
                etLastName.isEnabled = true
                etEmail.isEnabled = true
                etPhoneNumber.isEnabled = true
                btnCamera.isClickable = true
                btnPlace.isClickable = true
            }
            btnSave ->{
                btnEdit.visibility = View.VISIBLE
                btnSave.visibility = View.GONE
                etFirstname.isEnabled = false
                etLastName.isEnabled = false
                etEmail.isEnabled = false
                etPhoneNumber.isEnabled = false
                btnCamera.isClickable = false
                btnPlace.isClickable = false
            }
        }
    }
}
