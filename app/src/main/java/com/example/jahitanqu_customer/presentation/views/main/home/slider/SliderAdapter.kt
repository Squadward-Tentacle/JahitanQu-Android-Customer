package com.example.jahitanqu_customer.presentation.views.main.home.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.jahitanqu_customer.R
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

/**
 * Created by Maulana Ibrahim on 10/September/2020
 * Email maulibrahim19@gmail.com
 */
class SliderAdapter(private var listImage:MutableList<SliderItem>, val viewPager2: ViewPager2): RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {


    class SliderViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: RoundedImageView = view.findViewById(R.id.imageSlider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_slider,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        var data = listImage[position]
        if (data.image.isNotEmpty()){
            Picasso.get()
                .load(data.image)
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_photo)
                .into(holder.image)
        }
        if (position == listImage.size -2){
            viewPager2.post(runnable)
        }
    }

    val runnable = Runnable {
       run {
           listImage.addAll(listImage)
           notifyDataSetChanged()
       }
    }
}