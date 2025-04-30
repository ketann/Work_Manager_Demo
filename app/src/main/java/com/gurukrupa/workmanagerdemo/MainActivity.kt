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
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            //Below back office set linear it means time if request is failed then every 10 second 20 second 30 second and so one
            // In EXPONENTIAL if request is failed then 10, 20, 40, 80 like this retry
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()
        workManager.enqueue(request)
        //Below here we can set chain multiple option set
        workManager.beginWith(request).then(request).then(request)

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