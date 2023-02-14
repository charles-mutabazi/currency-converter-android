/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge

import android.app.Application
import com.paypay.xchange_challenge.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CurrencyXchangeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CurrencyXchangeApplication)
            modules(appModule)
        }
//        scheduleWork()
    }

//    @Inject
//    lateinit var workerFactory: HiltWorkerFactory
//
//    override fun getWorkManagerConfiguration() =
//        Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()
//
//    //schedule work to sync data every 30 minutes
//    @SuppressLint("InvalidPeriodicWorkRequestInterval")
//    private fun scheduleWork() {
//        val workRequest = PeriodicWorkRequest
//            .Builder(SyncWorker::class.java, REFRESH_INTERVAL, TimeUnit.MINUTES)
//            .build()
//
//        WorkManager.getInstance(applicationContext).enqueue(workRequest)
//    }
}