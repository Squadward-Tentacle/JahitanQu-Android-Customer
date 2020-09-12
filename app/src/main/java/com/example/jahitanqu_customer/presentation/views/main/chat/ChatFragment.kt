package com.example.jahitanqu_customer.presentation.views.main.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.views.main.chat.adapter.RecycleChatAdapter
import com.example.jahitanqu_customer.socket
import com.github.nkzawa.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_chat.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ChatFragment : Fragment(), View.OnClickListener {
    lateinit var recycleChatAdapter: RecycleChatAdapter
    var chatList = mutableListOf<com.example.jahitanqu_customer.model.Message>()
    lateinit var uniqueId: String
    val TAG = "ChatFragment"
    private var hasConnection = false
    private var startTyping = false
    private var time = 2
    private var thread2: Thread? = null

    @SuppressLint("HandlerLeak")
    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            Log.i(TAG, "handleMessage: typing stopped $startTyping")
            if (time == 0) {
                Log.i(TAG, "handleMessage: typing stopped time is $time")
                startTyping = false
                time = 2
            }
        }
    }


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
        rvChat.adapter = recycleChatAdapter
        btnSendChat.setOnClickListener(this)
        uniqueId = UUID.randomUUID().toString()

        if (savedInstanceState != null) {
            hasConnection = savedInstanceState.getBoolean("hasConnection")
        }

        if (hasConnection) {
        } else {
            socket.connect()
            socket.on("connect user", onNewUser)
            socket.on("chat message", onNewMessage)
            socket.on("on typing", onTyping)
            val userId = JSONObject()
            try {
                userId.put("username", prefs.keyFirstname + " Connected")
                socket.emit("connect user", userId)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

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
                val onTyping = JSONObject()
                try {
                    onTyping.put("typing", true)
                    onTyping.put("username", prefs.keyFirstname)
                    onTyping.put("uniqueId", uniqueId)
                    socket.emit("on typing", onTyping)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                btnSendChat.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }


    override fun onClick(p0: View?) {
        when (p0) {
            btnSendChat -> {
                Log.i(TAG, "sendMessage: ")
                val message: String = etChatText.text.toString().trim()
                if (TextUtils.isEmpty(message)) {
                    Log.i(TAG, "sendMessage:2 ")
                    return
                }
                etChatText.setText("")
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("message", message)
                    jsonObject.put("username", prefs.keyFirstname)
                    jsonObject.put("uniqueId", uniqueId)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                Log.i(TAG, "sendMessage: 1" + socket.emit("chat message", jsonObject))
            }
        }
    }

    var onNewMessage = Emitter.Listener { args ->
        activity?.runOnUiThread(Runnable {
            Log.i(TAG, "run: ")
            Log.i(TAG, "run: " + args.size)
            val data = args[0] as JSONObject
            val username: String
            val message: String
            val id: String
            try {
                username = data.getString("username")
                message = data.getString("message")
                id = data.getString("uniqueId")
                Log.i(TAG, "run: $username$message$id")
                chatList.add(
                    com.example.jahitanqu_customer.model.Message(
                        uniqueId = id,
                        username = username,
                        message = message
                    )
                )
                recycleChatAdapter = RecycleChatAdapter(chatList)
                recycleChatAdapter.notifyDataSetChanged()
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
            Log.i(TAG, "run: ")
            Log.i(TAG, "run: " + args.size)
            var username = args[0].toString()
            try {
                val `object` = JSONObject(username)
                username = `object`.getString("username")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            chatList.add(
                com.example.jahitanqu_customer.model.Message(
                    username = username
                )
            )
            Log.i(TAG, "run: $username")
        })
    }

    private var onTyping = Emitter.Listener { args ->
        activity?.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            Log.i(TAG, "run: " + args[0])
            try {
                var typingOrNot = data.getBoolean("typing")
                val userName =
                    data.getString("username") + " is Typing......"
                val id = data.getString("uniqueId")
                if (id == uniqueId) {
                    typingOrNot = false
                } else {
                }
                if (typingOrNot) {
                    if (!startTyping) {
                        startTyping = true
                        thread2 = Thread(
                            object : Runnable {
                                override fun run() {
                                    while (time > 0) {
                                        synchronized(this) {
                                            try {
                                                Log.i(
                                                    TAG,
                                                    "run: typing $time"
                                                )
                                            } catch (e: InterruptedException) {
                                                e.printStackTrace()
                                            }
                                            time--
                                        }
                                        handler.sendEmptyMessage(0)
                                    }
                                }
                            }
                        )
                        thread2!!.start()
                    } else {
                        time = 2
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
        val userId = JSONObject()
        try {
            userId.put("username", prefs.keyFirstname + " DisConnected")
            socket.emit("connect user", userId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        socket.disconnect()
        socket.off("chat message", onNewMessage)
        socket.off("connect user", onNewUser)
        socket.off("on typing", onTyping)

    }


}
