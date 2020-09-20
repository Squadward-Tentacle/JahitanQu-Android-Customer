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

class RecycleNearbyTailorAdapter(private val tailorList: List<Tailor>) :
    RecyclerView.Adapter<RecycleNearbyTailorViewHolder>() {

    lateinit var baseContract: BaseContract

    var showShimmer = true
    val shimmerItemNumber = 2

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleNearbyTailorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nearby_tailor, parent, false)
        return RecycleNearbyTailorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (showShimmer) shimmerItemNumber else tailorList.size
    }

    override fun onBindViewHolder(nearby: RecycleNearbyTailorViewHolder, position: Int) {
        if (showShimmer) {
            nearby.shimmerFrameLayout.startShimmer()
        } else {
            nearby.shimmerFrameLayout.stopShimmer()
            nearby.shimmerFrameLayout.setShimmer(null)

            var data = tailorList[position]
            nearby.imageTailor.background = null
            if (!data.imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(data.imageUrl)
                    .placeholder(R.drawable.ic_photo)
                    .error(R.drawable.ic_photo)
                    .into(nearby.imageTailor)
            }
            nearby.tailorAddress.background = null
            nearby.tailorAddress.text = data.address.addressName

            nearby.tailorName.background = null
            nearby.tailorName.text = "${data.firstname} ${data.lastname}"
            nearby.itemView.setOnClickListener {
                baseContract.itemClickListener(data.idTailor)
            }
        }

    }

}