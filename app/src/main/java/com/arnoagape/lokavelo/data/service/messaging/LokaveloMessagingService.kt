package com.arnoagape.lokavelo.data.service.messaging

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arnoagape.lokavelo.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LokaveloMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {

        val title = message.notification?.title
        val body = message.notification?.body

        showNotification(title, body)
    }

    private fun showNotification(title: String?, body: String?) {

        val notification = NotificationCompat.Builder(this, "messages")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(this)
            .notify(1, notification)
    }
}