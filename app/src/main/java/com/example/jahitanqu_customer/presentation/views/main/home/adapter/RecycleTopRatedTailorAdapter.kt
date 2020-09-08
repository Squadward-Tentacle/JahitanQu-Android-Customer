package com.example.jahitanqu_customer.presentation.views.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.BaseContract
import com.example.jahitanqu_customer.model.Tailor
import com.squareup.picasso.Picasso

/**
 * Created by Maulana Ibrahim on 07/September/2020
 * Email maulibrahim19@gmail.com
 */

class RecycleTopRatedTailorAdapter(private val tailorList:List<Tailor>) : RecyclerView.Adapter<RecycleTopRatedTailorViewHolder>(){

    lateinit var baseContract: BaseContract

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleTopRatedTailorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tailor_top_rated, parent, false)
        return RecycleTopRatedTailorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tailorList.size
    }

    override fun onBindViewHolder(holderTopRated: RecycleTopRatedTailorViewHolder, position: Int) {
        var data = tailorList[position]
        if (!data.avatarImageUrl.isNullOrEmpty()){
            Picasso.get()
                .load(data.avatarImageUrl)
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_photo)
                .into(holderTopRated.imageTailor)
        }
        holderTopRated.rating.rating = data.rating.toFloat()
        holderTopRated.tailorName.text = "${data.firstname} ${data.lastname}"
        holderTopRated.itemView.setOnClickListener {
            baseContract.itemClickListener(data.idTailor)
        }
    }

}