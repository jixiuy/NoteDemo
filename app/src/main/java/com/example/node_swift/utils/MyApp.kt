package com.example.node_swift.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import org.litepal.LitePal

class MyApp : Application() {

    companion object {
        lateinit var instance: MyApp
            private set

        fun getContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        LitePal.initialize(this);
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "todo_channel",
                "Todo Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Todo Reminders"
                setShowBadge(true)
                enableLights(true)
                lightColor = Color.Red.toArgb()
                enableVibration(true)
            }
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}