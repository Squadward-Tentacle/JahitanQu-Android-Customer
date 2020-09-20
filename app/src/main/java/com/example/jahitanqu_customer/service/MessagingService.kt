package com.example.jahitanqu_customer.service

import android.app.Notification
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.prefsFcmToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


/**
 * Created by Maulana Ibrahim on 09/September/2020
 * Email maulibrahim19@gmail.com
 */
class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        Log.d("TAG", "TOKEN : $p0")
        prefsFcmToken.keyCustomerFcm = p0
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notification = Notification.Builder(this)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setSmallIcon(R.drawable.ic_launcher)
            .build()
        val manager =
            NotificationManagerCompat.from(applicationContext)
        manager.notify(123, notification)
    }

}