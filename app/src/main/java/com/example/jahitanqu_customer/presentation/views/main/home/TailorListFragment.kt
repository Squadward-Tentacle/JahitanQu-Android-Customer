package com.example.jahitanqu_customer.presentation.views.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jahitanqu_customer.JahitanQu

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.presentation.viewmodel.TailorViewModel
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleTailorAdapter
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleTopRatedTailorAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_tailor_list.*
import javax.inject.Inject

class TailorListFragment : Fragment() {

    lateinit var recycleTailorAdapter: RecycleTailorAdapter

    @Inject
    lateinit var tailorViewModel: TailorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JahitanQu).applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tailor_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvListTailor.layoutManager = LinearLayoutManager(context)
        tailorViewModel.getTailor(1)
        tailorViewModel.tailorList.observe(viewLifecycleOwner, Observer { it ->
            recycleTailorAdapter = RecycleTailorAdapter(it)
            rvListTailor.adapter = recycleTailorAdapter
        })
    }
}
