package com.william.easykt

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.william.base_component.extension.logD
import com.william.base_component.extension.logI
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * ### author : WilliamYang
 * ### date : 2023/11/6 15:00
 * ### description : 模拟 [前台服务](https://developer.android.com/guide/components/foreground-services)
 */
class FGService : Service() {

    private val channelId = "fake_service"
    private val channelName = "default_channel"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        "FGService onCreate executed".logD()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(
                NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        notify(0)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        "FGService onStartCommand executed".logD()
        thread {
            "---FGService---start---".logI()
            val startNanoTime = System.nanoTime()

            var progress = 0
            while (progress < 100) {
                sleep(100)
                progress++
                "progress : $progress".logD()
                notify(progress)
            }

            val duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanoTime)
            "---FGService---end---duration: $duration ms , ${duration / 1000} s".logI()

            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun notify(progress: Int) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("FGService")
            .setProgress(100, progress, false)
            .setContentText("service is running")
            .setSilent(true)
            .setOngoing(true)
            .setSmallIcon(R.drawable.notification)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setContentIntent(pendingIntent)
        val notification = builder.build()
        // 开启前台服务
        startForeground(1, notification)
    }

    override fun onDestroy() {
        "FGService onDestroy executed".logD()
        super.onDestroy()
    }

}