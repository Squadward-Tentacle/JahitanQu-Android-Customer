package com.example.jahitanqu_customer.di.component

import com.example.jahitanqu_customer.di.module.FirebaseModule
import com.example.jahitanqu_customer.di.module.MidtransModule
import com.example.jahitanqu_customer.di.module.NetworkModule
import com.example.jahitanqu_customer.presentation.views.authentication.AuthActivity
import com.example.jahitanqu_customer.presentation.views.authentication.fragment.LoginFragment
import com.example.jahitanqu_customer.presentation.views.authentication.fragment.RegisterFragment
import com.example.jahitanqu_customer.presentation.views.main.MainActivity
import com.example.jahitanqu_customer.presentation.views.main.account.AccountFragment
import com.example.jahitanqu_customer.presentation.views.main.home.HomeFragment
import com.example.jahitanqu_customer.presentation.views.main.home.TailorDetailFragment
import com.example.jahitanqu_customer.presentation.views.main.home.TailorListFragment
import com.example.jahitanqu_customer.presentation.views.main.myorder.MyOrderActiveFragment
import com.example.jahitanqu_customer.presentation.views.main.myorder.MyOrderDetailFragment
import com.example.jahitanqu_customer.presentation.views.main.myorder.MyOrderHistoryFragment
import dagger.Component

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
@Component(modules = [FirebaseModule::class, NetworkModule::class,MidtransModule::class])
interface ApplicationComponent {
    fun inject(authActivity: AuthActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(registerFragment: RegisterFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(myOrderActiveFragment: MyOrderActiveFragment)
    fun inject(myOrderHistoryFragment: MyOrderHistoryFragment)
    fun inject(myOrderDetailFragment: MyOrderDetailFragment)
    fun inject(accountFragment: AccountFragment)
    fun inject(tailorListFragment: TailorListFragment)
    fun inject(tailorDetailFragment: TailorDetailFragment)
}