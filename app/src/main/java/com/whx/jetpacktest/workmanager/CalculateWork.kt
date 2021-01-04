package com.whx.jetpacktest.workmanager

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class CalculateWork(ctx: Context, workParam: WorkerParameters) : Worker(ctx, workParam) {

    companion object {
        const val CAL_TAG = "calculate_tag"
        const val KEY_INPUT_DATA = "originNum"
    }

    override fun doWork(): Result {
        val r: Long
        try {
            r = calculate(inputData.getLong(KEY_INPUT_DATA, 1))
            if (r < 0) {
                return Result.retry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }

        return Result.success(workDataOf("key_output" to r))
    }

    private fun calculate(seed: Long): Long {
        var res = seed
        for (i in 1..1000) {
            res *= i
        }
        return res
    }

    override fun onStopped() {
        Toast.makeText(applicationContext, "Calculate stop", Toast.LENGTH_SHORT).show()
    }
}