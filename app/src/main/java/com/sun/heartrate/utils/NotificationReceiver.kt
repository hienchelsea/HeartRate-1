package com.sun.heartrate.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationService::class.java)
        val pendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager
                .setExact(AlarmManager.RTC_WAKEUP, 1, pendingIntent)
        } else {
            alarmManager
                .set(AlarmManager.RTC_WAKEUP, 1, pendingIntent)
        }
    }
}