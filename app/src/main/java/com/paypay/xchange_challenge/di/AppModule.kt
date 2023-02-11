/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.di

import android.app.Application
import androidx.room.Room
import com.paypay.xchange_challenge.data.local.ExchangeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCurrencyExchangeDatabase(app: Application) = Room.databaseBuilder(
            app,
            ExchangeDatabase::class.java,
            "currency_exchange.db"
        ).build()

    @Singleton
    @Provides
    fun provideKtorClient(): HttpClient {
        val client = HttpClient(Android) {
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
        return client
    }
}