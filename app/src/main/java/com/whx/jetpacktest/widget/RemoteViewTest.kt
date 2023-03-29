package com.whx.jetpacktest.widget

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.whx.jetpacktest.R
import com.whx.jetpacktest.widget.imagepick.PhotosActivity

class RemoteViewTest {
    fun initNotification(context: Activity) {
        val views = RemoteViews(context.packageName, R.layout.layout_notification).apply {
            setTextColor(R.id.notif_title, Color.BLUE)
            setProgressBar(R.id.notif_progress, 100, 50, false)
        }

        val manager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "wtf"
        val builder = NotificationCompat.Builder(context, channelId)
        builder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(views)
//            .setTicker("3000000000 RMB")
//            .setContentTitle("tomato rich")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setWhen(System.currentTimeMillis() + 100)

        val intent = Intent(context, PhotosActivity::class.java)
        val requestId = 233
        val pendingIntent = PendingIntent.getActivity(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        builder.setContentIntent(pendingIntent)
        createNotificationChannel(context, channelId)
        manager.notify(requestId, builder.build())
    }

    private fun createNotificationChannel(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "my channel", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


}