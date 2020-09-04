package com.example.jahitanqu_customer.presentation.views.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.jahitanqu_customer.R
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
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, 1)
        viewPagerAdapter.addFragment(MyOrderActiveFragment(), "Active")
        viewPagerAdapter.addFragment(MyOrderHistoryFragment(), "History")
        view_pager.adapter = viewPagerAdapter
    }

    private class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {

        val listFragment = mutableListOf<Fragment>()
        val listString = mutableListOf<String>()

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
}
