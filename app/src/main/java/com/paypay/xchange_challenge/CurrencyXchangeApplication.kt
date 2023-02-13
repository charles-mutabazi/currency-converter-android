/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge

import android.annotation.SuppressLint
import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.paypay.xchange_challenge.util.REFRESH_INTERVAL
import com.paypay.xchange_challenge.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class CurrencyXchangeApplication : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        scheduleWork()
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    //schedule work to sync data every 30 minutes
    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    private fun scheduleWork() {
        val workRequest = PeriodicWorkRequest
            .Builder(SyncWorker::class.java, REFRESH_INTERVAL, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}