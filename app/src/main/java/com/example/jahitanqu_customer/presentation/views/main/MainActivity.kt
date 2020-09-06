package com.example.jahitanqu_customer.presentation.views.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R
import kotlinx.android.synthetic.main.activity_home.*


class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        (applicationContext as JahitanQu).applicationComponent.inject(this)

        navController = (nav_host_fragment_main as NavHostFragment).navController
        NavigationUI.setupWithNavController(bottom_navigation,navController)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.btnHome -> {
                    navController.navigate(R.id.toHomeFragment)
                    true
                }
                R.id.btnMyOrder -> {
                    navController.navigate(R.id.toMyOrderFragment)
                    true
                }
                R.id.btnAccount ->{
                    navController.navigate(R.id.toAccountFragment)
                    true
                }
                else ->{
                    navController.navigate(R.id.toHomeFragment)
                    true
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }
}
