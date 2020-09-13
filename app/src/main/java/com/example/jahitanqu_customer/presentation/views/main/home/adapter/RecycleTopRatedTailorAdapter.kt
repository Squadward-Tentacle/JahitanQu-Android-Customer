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

    var showShimmer = true
    val shimmerItemNumber = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleTopRatedTailorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tailor_top_rated, parent, false)
        return RecycleTopRatedTailorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (showShimmer) shimmerItemNumber else tailorList.size
    }

    override fun onBindViewHolder(holderTopRated: RecycleTopRatedTailorViewHolder, position: Int) {
        if (showShimmer){
            holderTopRated.shimmerFrameLayout.startShimmer()
        }else{
            holderTopRated.shimmerFrameLayout.stopShimmer()
            holderTopRated.shimmerFrameLayout.setShimmer(null)

            var data = tailorList[position]
            holderTopRated.imageTailor.background = null
            if (!data.imageUrl.isNullOrEmpty()){
                Picasso.get()
                    .load(data.imageUrl)
                    .placeholder(R.drawable.ic_photo)
                    .error(R.drawable.ic_photo)
                    .into(holderTopRated.imageTailor)
            }
            holderTopRated.rating.background = null
            holderTopRated.rating.rating = data.rating.toFloat()

            holderTopRated.tailorName.background = null
            holderTopRated.tailorName.text = "${data.firstname} ${data.lastname}"
            holderTopRated.itemView.setOnClickListener {
                baseContract.itemClickListener(data.idTailor)
            }
        }

    }

}