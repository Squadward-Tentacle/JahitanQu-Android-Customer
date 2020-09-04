package com.example.jahitanqu_customer.presentation.views.main.myorder.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {

    private val listFragment = mutableListOf<Fragment>()
    private val listString = mutableListOf<String>()

    fun addFragment(fragment: Fragment, title: String) {
        listFragment.add(fragment)
        listString.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listString[position]

    }

}