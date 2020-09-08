package com.example.jahitanqu_customer.presentation.views.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.model.Portofolio
import com.squareup.picasso.Picasso

/**
 * Created by Maulana Ibrahim on 08/September/2020
 * Email maulibrahim19@gmail.com
 */
class RecyclePortofolioAdapter(private val portofolioList: List<Portofolio>) :
    RecyclerView.Adapter<RecyclePortofolioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclePortofolioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_portofolio, parent, false)
        return RecyclePortofolioViewHolder(view)
    }

    override fun getItemCount(): Int {
        return portofolioList.size
    }

    override fun onBindViewHolder(holder: RecyclePortofolioViewHolder, position: Int) {
        val data = portofolioList[position]
        if (data?.imageUrl?.isNotEmpty()!!) {
            Picasso.get()
                .load(data.imageUrl)
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_photo)
                .into(holder.imagePortofolio)
        }
    }
}