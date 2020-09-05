package com.example.jahitanqu_customer.presentation.views.main.myorder.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.utils.util
import com.example.jahitanqu_customer.model.Transaction

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */

class MyOrderRecycleAdapter(private val transactionList: List<Transaction>) :
    RecyclerView.Adapter<MyOrderViewHolder>() {

    lateinit var myOrderClickListener: MyOrderClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.myorder_item, parent, false)
        return MyOrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: MyOrderViewHolder, position: Int) {
        holder.tailorName.text =
            "${transactionList[position].tailor.firstname} ${transactionList[position].tailor.lastname}"
        holder.transactionNumber.text = transactionList[position].idTransaction
        holder.transactionDate.text = transactionList[position].transactionDate
        holder.transactionStatus.text = util.convertStatusToString(transactionList[position].status)
        when (transactionList[position].status) {
            1 -> holder.transactionStatus.setTextColor(Color.parseColor("#EA0037"))
            6 -> holder.transactionStatus.setTextColor(Color.parseColor("#6CA7F6"))
            else -> holder.transactionStatus.setTextColor(Color.parseColor("#105E15"))
        }
        holder.itemView.setOnClickListener {
            myOrderClickListener.onItemClick(position)
        }
    }

}