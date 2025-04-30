package com.gurukrupa.workmanagerdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.gurukrupa.workmanagerdemo.worker.DemoWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val workManager = WorkManager.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        doWork()
    }

    private fun doWork() {
        val request = OneTimeWorkRequest.Builder(DemoWorker::class.java)
            .setConstraints( Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS )
            .build()
        workManager.enqueue(request)
        workManager.getWorkInfoByIdLiveData(request.id).observe(this) {
            if (it != null) {
                printStatus(it.state.name)
            }
        }
    }

    private fun printStatus(name: String) {
        Log.d("WorkManagerDemo1111", name)
    }
}