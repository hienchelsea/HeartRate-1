package com.sun.heartrate.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmUtils {
    
    fun setTurnOnRepeat(context: Context, requestCode: Int, timeInMillis: Long) {
        
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val timeNotification = if (System.currentTimeMillis() > timeInMillis) {
            timeInMillis + (timeInMillis - System.currentTimeMillis()) + 604800000
        } else {
            timeInMillis
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            timeNotification,
            604800000,
            pendingIntent
        )
        
    }
    
    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(context, NotificationReceiver::class.java)
        val pendingOnIntent = PendingIntent.getBroadcast(context, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingOnIntent)
    }
}