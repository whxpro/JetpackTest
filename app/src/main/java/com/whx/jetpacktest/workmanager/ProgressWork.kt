package com.whx.jetpacktest.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class ProgressWork(ctx: Context, workParam: WorkerParameters) : CoroutineWorker(ctx, workParam) {

    companion object {
        const val PROGRESS = "Progress"
        private const val delayDuration = 100L
    }

    override suspend fun doWork(): Result {
        val first = workDataOf(PROGRESS to 0)
        val last = workDataOf(PROGRESS to 100)

        setProgress(first)
        delay(delayDuration)
        setProgress(last)

        return Result.success()
    }
}