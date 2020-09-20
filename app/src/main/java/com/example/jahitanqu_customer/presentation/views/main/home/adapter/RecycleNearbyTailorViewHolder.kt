package com.example.jahitanqu_customer.presentation.views.main.home.adapter

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R
import com.facebook.shimmer.ShimmerFrameLayout

/**
 * Created by Maulana Ibrahim on 07/September/2020
 * Email maulibrahim19@gmail.com
 */
class RecycleNearbyTailorViewHolder(view: View):RecyclerView.ViewHolder(view) {
    val shimmerFrameLayout:ShimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)
    val imageTailor: ImageView = view.findViewById(R.id.ivTailor)
    val tailorName: TextView = view.findViewById(R.id.tvTailorName)
    val tailorAddress:TextView = view.findViewById(R.id.tvTailorAddress)
}