package com.development.mywebsiteconvertintoapp.fcm

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.development.mywebsiteconvertintoapp.MainActivity
import com.development.mywebsiteconvertintoapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.util.*

class MyFirebaseMessaging : FirebaseMessagingService() {
    private var numMessages = 0
    private val notificationChannelId = "100"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notification = remoteMessage.notification
        val data = remoteMessage.data
        sendNotification(notification, data)
    }

    private fun sendNotification(
            notification: RemoteMessage.Notification?,
            data: Map<String, String>
    ) {
        val bundle = Bundle()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtras(bundle)
        }
        val randId = gen()

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            addNextIntent(intent)
            getPendingIntent(randId, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationBuilder =
                NotificationCompat.Builder(this, notificationChannelId).apply {
                    setContentTitle(notification?.title)
                    setContentText(notification?.body)
                    setAutoCancel(true)
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    setContentIntent(resultPendingIntent)
                    setContentInfo("Hello")
                    setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background))
                    color = ContextCompat.getColor(applicationContext, R.color.black)
                    setLights(Color.RED, 1000, 300)
                    setDefaults(Notification.DEFAULT_VIBRATE)
                    setNumber(++numMessages)
                    setSmallIcon(R.drawable.ic_launcher_background)
                    setWhen(System.currentTimeMillis())
                }

        val notificationManager = NotificationManagerCompat.from(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    notificationChannelId,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESC
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(channel)
        }
        assert(notificationManager != null)
        notificationManager.notify(randId, notificationBuilder.build())
    }

    private fun gen(): Int {
        val r = Random(System.currentTimeMillis())
        return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000)
    }

    companion object {
        const val FCM_PARAM = "picture"
        private const val CHANNEL_NAME = "FCM"
        private const val CHANNEL_DESC = "Firebase Cloud Messaging"
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}