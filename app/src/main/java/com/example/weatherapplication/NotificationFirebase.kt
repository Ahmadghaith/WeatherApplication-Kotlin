package com.example.weatherapplication

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationFirebase: FirebaseMessagingService() {


        companion object
        {
            @JvmStatic
            val CHANNEL_ID = "1"
        }

        override fun onNewToken(token:String)
        {
            super.onNewToken(token);
        }

        override fun onMessageReceived(remoteMessage: RemoteMessage)
        {
            super.onMessageReceived(remoteMessage)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this))
            {
                notify(3, builder.build())
            }

            if (remoteMessage.notification!!.title =="Weather notification")
            {
                Log.d("Notification", "This a notification from Firebase")
            }

        }
}
