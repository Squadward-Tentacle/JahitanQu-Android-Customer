package com.example.jahitanqu_customer.presentation.views.main.myorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.BaseContract
import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.model.Transaction
import com.example.jahitanqu_customer.presentation.viewmodel.TransactionViewModel
import com.example.jahitanqu_customer.presentation.views.main.myorder.adapter.MyOrderClickListener
import com.example.jahitanqu_customer.presentation.views.main.myorder.adapter.MyOrderRecycleAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_my_order.*
import kotlinx.android.synthetic.main.fragment_my_order_active.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import javax.inject.Inject


class MyOrderActiveFragment : Fragment(), BaseContract {

    lateinit var myOrderRecycleAdapter: MyOrderRecycleAdapter
    lateinit var navController: NavController

    @Inject
    lateinit var transactionViewModel: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JahitanQu).applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_order_active, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        rvOrderActive.layoutManager = LinearLayoutManager(context)
        myOrderRecycleAdapter = MyOrderRecycleAdapter()
        transactionViewModel.transactionPagedList.observe(viewLifecycleOwner, Observer { it ->
            myOrderRecycleAdapter.submitList(it)
            myOrderRecycleAdapter.baseContract = this
            rvOrderActive.adapter = myOrderRecycleAdapter
        })

        transactionViewModel.showShimmerTransactionActive.observe(viewLifecycleOwner, Observer {
            if (it){
                rvOrderActive.visibility = View.GONE
                shimmerFrameLayout.visibility = View.VISIBLE
                shimmerFrameLayout.startShimmer()
            }else{
                rvOrderActive.visibility = View.VISIBLE
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmer()
                transactionViewModel.isEmpty.observe(viewLifecycleOwner, Observer {
                    if (it){
                        ivEmptyList.visibility = View.VISIBLE
                    }else{
                        ivEmptyList.visibility = View.GONE
                    }
                })
            }
        })
    }

    override fun itemClickListener(id: String) {
        val bundle = bundleOf(Constant.KEY_ID_TRANSACTION to id)
        navController.navigate(R.id.toMyOrderDetailFragment,bundle)
    }
}
