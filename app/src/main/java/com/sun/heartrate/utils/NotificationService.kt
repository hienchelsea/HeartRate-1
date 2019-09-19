package com.sun.heartrate.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.sun.heartrate.R
import com.sun.heartrate.ui.main.MainActivity

class NotificationService : IntentService("Heart") {
    override fun onHandleIntent(intent: Intent?) {
        val intentFlag = Intent.FLAG_ACTIVITY_CLEAR_TOP or
            Intent.FLAG_ACTIVITY_SINGLE_TOP or
            Intent.FLAG_ACTIVITY_CLEAR_TASK or
            Intent.FLAG_ACTIVITY_NEW_TASK
        val intentNotification = MainActivity.getIntent(this).apply {
            intentFlag
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intentNotification,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(
                Constant.CHANNEL_ID,
                Constant.CHANNEL_NAME,
                importance
            )
            notificationChannel.description = Constant.CHANNEL_DESCRIPTION
            getSystemService(
                NotificationManager::class.java
            ).createNotificationChannel(notificationChannel)
        }
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, getNotification(pendingIntent))
    }
    
    private fun getNotification(
        pendingIntent: PendingIntent
    ): Notification = NotificationCompat.Builder(
        this, Constant.CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_general_red)
        .setContentTitle(getString(R.string.app_name))
        .setContentText(getString(R.string.title_notification))
        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
        .setDefaults(Notification.DEFAULT_SOUND)
        .setAutoCancel(true)
        .setPriority(6)
        .setContentIntent(pendingIntent)
        .build()
}

