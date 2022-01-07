package com.example.weatherapplication

import android.app.AlertDialog
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
class NotificationFirebase: FirebaseMessagingService() {


        companion object
        {
            const val CHANNEL_ID = "1"
        }

        override fun onNewToken(token:String)
        {
            super.onNewToken(token)
        }

        override fun onMessageReceived(remoteMessage: RemoteMessage)
        {
            super.onMessageReceived(remoteMessage)

            val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_weather_foreground)
                .setContentTitle(remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this))
            {
                notify(3, notificationBuilder.build())
            }

            if (remoteMessage.notification!!.title =="WeatherApplication")
            {
                //Alert Dialog
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Notification")
                builder.setMessage("Notification is working! 🔥")

                builder.setPositiveButton(android.R.string.yes) { _, _ ->
                    Toast.makeText(applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                }
                builder.show()
            }

           /* when (remoteMessage.notification!!.title) {
                "Current weather" -> {
                    val intent = Intent(this, TodayFragment::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                    startActivity(intent)
                }
                "7 days forecast" -> {
                    val intent = Intent(this, ForecastFragment::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                    startActivity(intent)
                }
                "Maps" -> {
                    val intent = Intent(this, GoogleMapsFragment::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                    startActivity(intent)
                }
            }*/

        }
}
