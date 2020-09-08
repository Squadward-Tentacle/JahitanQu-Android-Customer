package com.example.jahitanqu_customer.presentation.views.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.model.Comment

/**
 * Created by Maulana Ibrahim on 08/September/2020
 * Email maulibrahim19@gmail.com
 */
class RecycleCommentAdapter(val commentList: List<Comment>) :
    RecyclerView.Adapter<RecycleCommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleCommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rating_and_review, parent, false)
        return RecycleCommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: RecycleCommentViewHolder, position: Int) {
        val data = commentList[position]
        holder.customerName.text = "${data.firstname} ${data.lastname}"
        holder.customerRating.rating = data.rating.toFloat()
        holder.customerComment.text = data.comment
    }
}