package com.example.jahitanqu_customer.presentation.views.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.BaseContract
import com.example.jahitanqu_customer.model.Tailor
import com.squareup.picasso.Picasso

/**
 * Created by Maulana Ibrahim on 07/September/2020
 * Email maulibrahim19@gmail.com
 */
class RecycleTailorAdapter : PagedListAdapter<Tailor, RecycleTailorViewHolder>(
    DIFF_CALLBACK
) {

    lateinit var baseContract: BaseContract

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleTailorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tailor, parent, false)
        return RecycleTailorViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecycleTailorViewHolder, position: Int) {
        var data = getItem(position)
        if (data?.avatarImageUrl?.isNotEmpty()!!) {
            Picasso.get()
                .load(data.avatarImageUrl)
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_photo)
                .into(holder.imageTailor)
        }
        holder.rating.rating = data.rating.toFloat()
        holder.tailorName.text = "${data.firstname} ${data.lastname}"
        holder.tailorAddress.text = data.address.addressName
        holder.itemView.setOnClickListener {
            baseContract.itemClickListener(data.idTailor)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Tailor>() {
            override fun areItemsTheSame(oldItem: Tailor, newItem: Tailor): Boolean {
                return oldItem.idTailor == newItem.idTailor
            }

            override fun areContentsTheSame(oldItem: Tailor, newItem: Tailor): Boolean {
                return oldItem == newItem
            }

        }
    }


}