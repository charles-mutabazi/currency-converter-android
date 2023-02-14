/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge

import android.app.Application
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.paypay.xchange_challenge.di.appModule
import com.paypay.xchange_challenge.worker.SyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class CurrencyXchangeApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CurrencyXchangeApplication)
            workManagerFactory()
            modules(appModule)
        }
        scheduleWork()
    }

    //schedule work to sync data every 30 minutes
    private fun scheduleWork() {
        val workRequest = PeriodicWorkRequest
            .Builder(SyncWorker::class.java, 15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}