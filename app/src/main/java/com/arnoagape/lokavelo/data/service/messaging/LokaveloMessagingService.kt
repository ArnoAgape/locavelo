package com.arnoagape.lokavelo.data.service.messaging

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arnoagape.lokavelo.MainActivity
import com.arnoagape.lokavelo.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LokaveloMessagingService : FirebaseMessagingService() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onMessageReceived(message: RemoteMessage) {

        val title = message.notification?.title
        val body = message.notification?.body
        val conversationId = message.data["conversationId"]

        showNotification(title, body, conversationId)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(
        title: String?,
        body: String?,
        conversationId: String?
    ) {

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("conversationId", conversationId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "messages")
            .setContentTitle(title)
            .setContentText(body)
            .setColorized(true)
            .setSmallIcon(R.drawable.ic_velo_logo)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(1, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("New token FCM : $token")
    }
}