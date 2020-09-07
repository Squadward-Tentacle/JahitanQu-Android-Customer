package com.example.jahitanqu_customer.presentation.views.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.model.Tailor
import com.squareup.picasso.Picasso

/**
 * Created by Maulana Ibrahim on 07/September/2020
 * Email maulibrahim19@gmail.com
 */

class RecycleTailorAdapter(private val tailorList:List<Tailor>) : RecyclerView.Adapter<RecycleTailorViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleTailorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tailor_top_rated, parent, false)
        return RecycleTailorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tailorList.size
    }

    override fun onBindViewHolder(holder: RecycleTailorViewHolder, position: Int) {
        var data = tailorList[position]
        if (!data.avatarImageUrl.isNullOrEmpty()){
            Picasso.get()
                .load(data.avatarImageUrl)
                .placeholder(R.drawable.ic_user_placehoder)
                .error(R.drawable.ic_user_placehoder)
                .into(holder.imageTailor)
        }
        holder.rating.rating = data.rating.toFloat()
        holder.tailorName.text = "${data.firstname} ${data.lastname}"
    }

}