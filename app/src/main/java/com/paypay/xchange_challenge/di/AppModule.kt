/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.di

import androidx.room.Room
import com.paypay.xchange_challenge.data.local.ExchangeDatabase
import com.paypay.xchange_challenge.data.repository_impl.ExchangeListingRepositoryImpl
import com.paypay.xchange_challenge.domain.repository.ExchangeRepository
import com.paypay.xchange_challenge.presentation.home.HomeViewModel
import com.paypay.xchange_challenge.worker.SyncWorker
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ExchangeDatabase::class.java,
            "currency_exchange.db"
        ).build()
    }
    single {
        HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 2000
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    single<ExchangeRepository> {
        ExchangeListingRepositoryImpl(get(), get())
    }

    viewModel {
        HomeViewModel(get())
    }

    worker { SyncWorker(get(), get(), get()) }
}
