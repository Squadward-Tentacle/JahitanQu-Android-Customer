package com.example.jahitanqu_customer.presentation.views.main.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.presentation.views.main.chat.adapter.RecycleChatAdapter
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment(),View.OnClickListener {
    lateinit var recycleChatAdapter: RecycleChatAdapter
    var chatList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvChat.layoutManager = LinearLayoutManager(context)
        recycleChatAdapter = RecycleChatAdapter(chatList)
        btnSendChat.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0){
            btnSendChat ->{
                val message = etChatText.text.toString()
                if (message.isNotEmpty()){
                    chatList.add(message)
                    recycleChatAdapter.notifyDataSetChanged()
                }
            }
        }
    }


}
