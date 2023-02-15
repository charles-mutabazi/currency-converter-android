/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.data.repository_impl

import android.util.Log
import com.paypay.xchange_challenge.data.local.ExchangeDatabase
import com.paypay.xchange_challenge.data.mapper.toCurrencyListing
import com.paypay.xchange_challenge.data.mapper.toCurrencyListingEntity
import com.paypay.xchange_challenge.data.remote.dto.RateDTO
import com.paypay.xchange_challenge.domain.model.CurrencyListing
import com.paypay.xchange_challenge.domain.repository.ExchangeRepository
import com.paypay.xchange_challenge.util.APP_ID
import com.paypay.xchange_challenge.util.BASE_URL
import com.paypay.xchange_challenge.util.Resource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class ExchangeListingRepositoryImpl(
    private val db: ExchangeDatabase,
    private val client: HttpClient
) : ExchangeRepository {
    override fun getCurrencyList(fetchFromRemote: Boolean): Flow<Resource<List<CurrencyListing>>> =
        flow {
            emit(Resource.Loading(true))
            val localListings = db.currencyDao.getCurrencyListings()

            val shouldLoadFromCache = !fetchFromRemote && localListings.isNotEmpty()

            if (shouldLoadFromCache) {
                emit(Resource.Success(localListings.map { it.toCurrencyListing() }))
                return@flow
            }

            // cache the result to local db
            if (getRemoteCurrencies() != null) {
                db.currencyDao.insertCurrencyListing(getRemoteCurrencies()!!.map {
                    it.toCurrencyListingEntity()
                })

                //update the rates
                getLatestRates()?.let { rates ->
                    db.currencyDao.updateCurrencyListingTx(rates)
                }

                emit(Resource.Success(
                    db.currencyDao.getCurrencyListings().map { it.toCurrencyListing() }
                ))
            } else {
                emit(Resource.Error("An unexpected error occurred"))
            }
        }


    //this should go in remote data source
    suspend fun getRemoteCurrencies(): List<CurrencyListing>? {
        return try {
            val response =
                client.get("$BASE_URL/currencies.json").body<Map<String, String>>()
            response.map {
                CurrencyListing(symbol = it.key, rate = 0.0, name = it.value)
            }
        } catch (e: IOException) {
            Log.e("ExchangeRepositoryImpl", "getRemoteCurrencies: ${e.message}")
            null
        }
    }

    /**
     * Get the latest rates from the api
     */
    suspend fun getLatestRates(): List<Map<String, Double>>? {
        return try {
            val response = client.get("$BASE_URL/latest.json?app_id=$APP_ID")
                .body<RateDTO>()
            response.rates.map {
                mapOf(it.key to it.value)
            }
        } catch (e: IOException) {
            Log.e("ExchangeRepositoryImpl", "getLatestRates: ${e.message}")
            null
        }
    }
}
