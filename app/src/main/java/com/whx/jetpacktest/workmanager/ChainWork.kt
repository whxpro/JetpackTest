package com.whx.jetpacktest.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class CalWork1(ctx: Context, workParam: WorkerParameters) : Worker(ctx, workParam) {
    override fun doWork(): Result {

        return Result.success(workDataOf("random" to 233))
    }
}

class CalWork2(ctx: Context, workParam: WorkerParameters) : Worker(ctx, workParam) {
    override fun doWork(): Result {
        return Result.success()
    }
}

class CalWork3(ctx: Context, workParam: WorkerParameters) : Worker(ctx, workParam) {
    override fun doWork(): Result {
        return Result.success()
    }
}

class CalWork4(ctx: Context, workParam: WorkerParameters) : Worker(ctx, workParam) {
    override fun doWork(): Result {
        return Result.success()
    }
}

class CalWork5(ctx: Context, workParam: WorkerParameters) : Worker(ctx, workParam) {
    override fun doWork(): Result {
        return Result.success()
    }
}