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
        val view = inflater.inflate(R.layout.fragment_my_order, container, false)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
