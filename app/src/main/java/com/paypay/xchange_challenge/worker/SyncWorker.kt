/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 12/02/2023
 */
package com.paypay.xchange_challenge.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paypay.xchange_challenge.domain.repository.ExchangeRepository

class SyncWorker constructor(
    appContext: Context,
    workerParams: WorkerParameters,
    private val exchangeListRepository: ExchangeRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        //a worker to sync data every 30 minutes
        Log.i("SyncWorker", "Fetching data from remote...")
        exchangeListRepository.getCurrencyList(fetchFromRemote = true)
        return Result.success()
    }
}
