package com.example.jahitanqu_customer.views.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        (applicationContext as JahitanQu).applicationComponent.inject(this)
    }
}
