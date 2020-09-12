package com.example.jahitanqu_customer.presentation.views.main.myorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.views.main.myorder.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_order.*


class MyOrderFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return if (prefs.keyIdCustomer.isNullOrEmpty()){
            inflater.inflate(R.layout.item_not_register, container, false)
        }else{
            (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
            inflater.inflate(R.layout.fragment_my_order, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (prefs.keyIdCustomer?.isNotEmpty()!!){
            tab_layout.setupWithViewPager(view_pager, true)
            val viewPagerAdapter =
                ViewPagerAdapter(
                    childFragmentManager,
                    1
                )
            viewPagerAdapter.addFragment(MyOrderActiveFragment(), "Active")
            viewPagerAdapter.addFragment(MyOrderHistoryFragment(), "History")
            view_pager.adapter = viewPagerAdapter
        }
    }


}
