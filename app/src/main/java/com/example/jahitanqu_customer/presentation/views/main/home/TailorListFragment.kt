package com.example.jahitanqu_customer.presentation.views.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jahitanqu_customer.JahitanQu

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.BaseContract
import com.example.jahitanqu_customer.presentation.viewmodel.TailorViewModel
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleTailorAdapter
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleTopRatedTailorAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_tailor_list.*
import javax.inject.Inject

class TailorListFragment : Fragment(),BaseContract {

    lateinit var recycleTailorAdapter: RecycleTailorAdapter
    lateinit var navController: NavController

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
        return inflater.inflate(R.layout.fragment_tailor_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        rvListTailor.layoutManager = LinearLayoutManager(context)
        recycleTailorAdapter = RecycleTailorAdapter()
        recycleTailorAdapter.baseContract = this
        tailorViewModel.tailorPagedList.observe(viewLifecycleOwner, Observer {
            recycleTailorAdapter.submitList(it)
        })
        rvListTailor.adapter = recycleTailorAdapter
    }

    override fun itemClickListener(id: String) {
        val bundle = bundleOf("idTailor" to id)
        navController.navigate(R.id.toTailorDetailFragment,bundle)
    }
}
