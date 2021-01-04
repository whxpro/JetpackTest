package com.whx.jetpacktest.workmanager

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.*
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import kotlinx.android.synthetic.main.activity_work_test.*
import java.util.concurrent.TimeUnit

class WorkTestActivity : BaseActivity() {

    private lateinit var mWorkManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWorkManager = WorkManager.getInstance(this)

        setContentView(R.layout.activity_work_test)

        startCalculate.setOnClickListener {
            makeOneTimeTask()
        }

        getWorkInfosByTag(CalculateWork.CAL_TAG).observe(this, Observer { workList ->
            workList.forEach {
                if (it.tags.contains(CalculateWork.CAL_TAG)) {
                    Toast.makeText(this, it.outputData.getLong("key_output", 0).toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun makeOneTimeTask() {
        // 约束
        // 如果指定了多个约束，工作将仅在满足所有约束时才会运行。
        // 如果在工作运行时不再满足某个约束，WorkManager 将停止工作器。系统将在满足所有约束后重试工作。
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)          // 仅在WiFi 网络
            .setRequiresCharging(true)                              // 仅在充电情况
            .build()

        val workRequest = OneTimeWorkRequestBuilder<CalculateWork>()
            /*.setConstraints(constraint)*/
            /*.setInitialDelay(1L, TimeUnit.MINUTES)*/         // 如果工作没有约束，或者当工作加入队列时所有约束都得到了满足，那么系统可能会选择立即运行该工作。如果不希望工作立即运行，可以将工作指定为在经过一段最短初始延迟时间后再启动。
            .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)     // 如果doWork 返回Result.retry()，系统会根据退避延迟时间 和 退避政策重试work，本例中延时时间10s
            .addTag(CalculateWork.CAL_TAG)
            .setInputData(workDataOf(
                CalculateWork.KEY_INPUT_DATA to 1
            ))
            .build()

        mWorkManager.cancelAllWorkByTag(CalculateWork.CAL_TAG)
        mWorkManager.enqueue(workRequest)
    }

    private fun makePeriodTask() {
        val workRequest = PeriodicWorkRequestBuilder<TimedWork>(15, TimeUnit.MINUTES).build()

        mWorkManager.enqueueUniquePeriodicWork("attention", ExistingPeriodicWorkPolicy.KEEP, workRequest) // 唯一工作，避免enqueue 多个相同工作
    }

    private fun cancelTask(tag: String) {
        mWorkManager.cancelAllWorkByTag(tag)
    }

    private fun getWorkInfosByTag(tag: String): LiveData<List<WorkInfo>> {
        return mWorkManager.getWorkInfosByTagLiveData(tag)
    }

    private fun getWorkInfos(tag: String): LiveData<List<WorkInfo>> {
        val workQuery = WorkQuery.Builder
            .fromTags(listOf(tag))
            .addStates(listOf(WorkInfo.State.FAILED, WorkInfo.State.CANCELLED))
            .addUniqueWorkNames(listOf("preProcess", "sync"))
            .build()

        return mWorkManager.getWorkInfosLiveData(workQuery)
    }

    private fun watchProgress() {
        val progressWorkRequest = OneTimeWorkRequestBuilder<ProgressWork>().build()
        mWorkManager.enqueue(progressWorkRequest)

        mWorkManager.getWorkInfoByIdLiveData(progressWorkRequest.id)
            .observe(this, Observer {
                if (it != null) {
                    val progress = it.progress
                    val value = progress.getInt(ProgressWork.PROGRESS, 0)

                    text?.text = value.toString()
                }
            })
    }

    private fun makeChain() {
        val cWork1 = OneTimeWorkRequestBuilder<CalWork1>().build()
        val cWork2 = OneTimeWorkRequestBuilder<CalWork2>().build()
        val cWork3 = OneTimeWorkRequestBuilder<CalWork3>().build()
        val cWork4 = OneTimeWorkRequestBuilder<CalWork4>().setInputMerger(ArrayCreatingInputMerger::class).build()
        val cWork5 = OneTimeWorkRequestBuilder<CalWork5>().build()


        mWorkManager.beginWith(listOf(cWork1, cWork2, cWork3))
            .then(cWork4)
            .then(cWork5)
            .enqueue()
    }
}