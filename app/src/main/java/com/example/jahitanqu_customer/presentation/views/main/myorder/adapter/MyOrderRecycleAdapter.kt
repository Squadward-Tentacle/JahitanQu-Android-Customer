package com.example.jahitanqu_customer.presentation.views.main.myorder.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.BaseContract
import com.example.jahitanqu_customer.common.utils.Util
import com.example.jahitanqu_customer.model.Tailor
import com.example.jahitanqu_customer.model.Transaction

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
class MyOrderRecycleAdapter:PagedListAdapter<Transaction,MyOrderViewHolder>(DIFF_CALLBACK){

    lateinit var baseContract: BaseContract

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_myorder, parent, false)
        return MyOrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyOrderViewHolder, position: Int) {
        var transaction = getItem(position)
        holder.tailorName.text = transaction?.firstnameTailor
        holder.transactionNumber.text = transaction?.idTransaction
        holder.transactionDate.text = transaction?.transactionDate
        holder.transactionStatus.text = transaction?.statusName
        when (transaction?.status) {
            1 -> holder.transactionStatus.setTextColor(Color.parseColor("#EA0037"))
            6 -> holder.transactionStatus.setTextColor(Color.parseColor("#6CA7F6"))
            else -> holder.transactionStatus.setTextColor(Color.parseColor("#105E15"))
        }
        holder.itemView.setOnClickListener {
            baseContract.itemClickListener(transaction?.idTransaction!!)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Transaction>() {

            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return  oldItem.idTransaction == newItem.idTransaction
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem == newItem
            }

        }
    }
}