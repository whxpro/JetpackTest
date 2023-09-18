package com.whx.jetpacktest.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.whx.jetpacktest.R

class DownloadWorker(ctx: Context, workParam: WorkerParameters) : CoroutineWorker(ctx, workParam) {
    companion object {
        const val KEY_INPUT_URL = "KEY_INPUT_URL"
        const val KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME"
    }

    private val notiManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        val inputUrl = inputData.getString(KEY_INPUT_URL) ?: return Result.failure()
        val outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME) ?: return Result.failure()

        val progress = "Starting Download"
        setForeground(createForegroundInfo(progress))
        download(inputUrl, outputFile)
        return Result.success()
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val id = "notify_id"
        val title = "Download Task"
        val cancel = "Cancel"

        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(getId())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .addAction(R.mipmap.ic_launcher, cancel, intent)
            .build()

        val notifId = 233
        return ForegroundInfo(notifId, notification)
    }

    private fun download(inputUrl: String, outputFile: String) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = "download"
        val desc = "download something"
        val channel = NotificationChannel("download_channel", name, NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = desc
        }

        notiManager.createNotificationChannel(channel)
    }
}