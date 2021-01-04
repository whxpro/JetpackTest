package com.whx.jetpacktest.workmanager

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

class TimedWork(ctx: Context, workParam: WorkerParameters) : Worker(ctx, workParam) {

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }

    override fun onStopped() {
        Toast.makeText(applicationContext, "Timed work stop", Toast.LENGTH_SHORT).show()
    }
}