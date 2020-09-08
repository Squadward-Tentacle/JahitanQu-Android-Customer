package com.example.jahitanqu_customer.presentation.views.main.home.adapter

import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Maulana Ibrahim on 08/September/2020
 * Email maulibrahim19@gmail.com
 */
class RecycleCommentViewHolder(view:View):RecyclerView.ViewHolder(view) {
    val customerImageUrl: CircleImageView = view.findViewById(R.id.ivCustomerImageUrl)
    val customerName : TextView = view.findViewById(R.id.tvCustomerName)
    val customerRating:RatingBar = view.findViewById(R.id.rbCustomerRating)
    val customerComment:TextView = view.findViewById(R.id.tvCustomerComment)

}