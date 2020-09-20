package com.example.jahitanqu_customer.presentation.views.main.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.views.main.chat.adapter.RecycleChatAdapter
import com.example.jahitanqu_customer.socket
import com.github.nkzawa.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_chat.*
import org.json.JSONException


class ChatFragment : Fragment(), View.OnClickListener {
    lateinit var recycleChatAdapter: RecycleChatAdapter
    lateinit var idTailor: String
    lateinit var navController: NavController
    var chatList = mutableListOf<com.example.jahitanqu_customer.model.Message>()
    val TAG = "ChatFragment"
    private var hasConnection = false


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
        idTailor = arguments?.getString(Constant.KEY_ID_TAILOR)!!
        if (savedInstanceState != null) {
            hasConnection = savedInstanceState.getBoolean("hasConnection")
        }

        if (!hasConnection) {
            socket.connect()
            socket.on("join room", onNewUser)
            socket.on("chat message", onNewMessage)
            try {
                socket.emit("join room", idTailor)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        hasConnection = true

        rvChat.layoutManager = LinearLayoutManager(context)
        recycleChatAdapter = RecycleChatAdapter(chatList)
        rvChat.adapter = recycleChatAdapter

        navController = Navigation.findNavController(view)
        btnSendChat.setOnClickListener(this)
        btnBack.setOnClickListener(this)

        onTypeButtonEnable()
    }


    private fun onTypeButtonEnable() {
        etChatText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                btnSendChat.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }


    override fun onClick(p0: View?) {
        when (p0) {
            btnSendChat -> {
                val message: String = etChatText.text.toString().trim()
                if (TextUtils.isEmpty(message)) {
                    return
                }
                etChatText.setText("")
                Log.i(
                    TAG,
                    "sendMessage: 1" + socket.emit(
                        "chat message",
                        message,
                        prefs.keyFirstname,
                        idTailor
                    )
                )
                chatList.add(
                    com.example.jahitanqu_customer.model.Message(
                        "1",
                        prefs.keyFirstname!!,
                        message
                    )
                )
                recycleChatAdapter = RecycleChatAdapter(chatList)
                recycleChatAdapter.notifyDataSetChanged()
                rvChat.scrollToPosition(chatList.size - 1)
            }
            btnBack -> {
                activity?.onBackPressed()
            }
        }
    }

    private var onNewMessage = Emitter.Listener { args ->
        activity?.runOnUiThread(Runnable {
            try {
                chatList.add(
                    com.example.jahitanqu_customer.model.Message(
                        "2",
                        args[1].toString(),
                        args[0].toString()
                    )
                )
                recycleChatAdapter = RecycleChatAdapter(chatList)
                recycleChatAdapter.notifyDataSetChanged()
                rvChat.scrollToPosition(chatList.size - 1)
                Log.i(TAG, "run:5 ")
            } catch (e: Exception) {
                return@Runnable
            }
        })
    }

    private var onNewUser = Emitter.Listener { args ->
        activity?.runOnUiThread(Runnable {
            val length = args.size
            if (length == 0) {
                return@Runnable
            }
            var username = args[0].toString()
            Log.i(TAG, "run: $username")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
        socket.disconnect()
        socket.off("chat message", onNewMessage)
        socket.off("join room", onNewUser)
    }
}
