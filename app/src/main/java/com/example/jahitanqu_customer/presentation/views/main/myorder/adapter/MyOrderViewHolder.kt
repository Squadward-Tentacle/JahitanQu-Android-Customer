package com.example.jahitanqu_customer.presentation.views.main.myorder.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R

/**
 * Created by Maulana Ibrahim on 05/September/2020
 * Email maulibrahim19@gmail.com
 */
class MyOrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tailorName: TextView = view.findViewById(R.id.tvTailorName)
    val transactionNumber: TextView = view.findViewById(R.id.tvTransactionNumber)
    val transactionDate: TextView = view.findViewById(R.id.tvTransactionDate)
    val transactionStatus: TextView = view.findViewById(R.id.tvTransactionStatus)
}