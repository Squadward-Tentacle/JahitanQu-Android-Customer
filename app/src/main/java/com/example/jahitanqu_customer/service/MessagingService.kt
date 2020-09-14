package com.example.jahitanqu_customer.service

import android.util.Log
import com.example.jahitanqu_customer.prefs
import com.google.firebase.messaging.FirebaseMessagingService

/**
 * Created by Maulana Ibrahim on 09/September/2020
 * Email maulibrahim19@gmail.com
 */
class MessagingService: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        Log.d("TAG","TOKEN : $p0")
        prefs.keyCustomerFcm = p0
    }

}