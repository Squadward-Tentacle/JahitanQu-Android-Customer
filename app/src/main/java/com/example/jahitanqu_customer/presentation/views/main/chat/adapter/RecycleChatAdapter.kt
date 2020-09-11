package com.example.jahitanqu_customer.presentation.views.main.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleTailorViewHolder

/**
 * Created by Maulana Ibrahim on 11/September/2020
 * Email maulibrahim19@gmail.com
 */
class RecycleChatAdapter(private val chatList:List<String>):RecyclerView.Adapter<RecycleChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return RecycleChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: RecycleChatViewHolder, position: Int) {
        holder.chatText.text = chatList[position]
    }
}