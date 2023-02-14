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
    db: ExchangeDatabase,
    private val client: HttpClient
) : ExchangeRepository {
    private val dao = db.currencyDao
    override fun getCurrencyList(fetchFromRemote: Boolean): Flow<Resource<List<CurrencyListing>>> =
        flow {
            emit(Resource.Loading(true))
            val localListings = dao.getCurrencyListings()

            val shouldLoadFromCache = !fetchFromRemote && localListings.isNotEmpty()

        if (shouldLoadFromCache) {
            emit(Resource.Success(localListings.map { it.toCurrencyListing() }))
            return@flow
        }

        //Get from remote using Ktor
        val remoteCurrencies = try {
            val response =
                client.get("$BASE_URL/currencies.json").body<Map<String, String>>()
            response.map {
                CurrencyListing(symbol = it.key, rate = 0.0, name = it.value)
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
            null
        }

        // cache the result to local db
        remoteCurrencies?.let { listing ->
            dao.insertCurrencyListing(listing.map {
                it.toCurrencyListingEntity()
            })

            //update the rates
            getLatestRates()?.let { rates ->
                dao.updateCurrencyListingTx(rates)
            }

            emit(Resource.Success(
                data = dao.getCurrencyListings().map { it.toCurrencyListing() }
            ))
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

    override fun deleteCurrencyTable() = flow {
        dao.deleteCurrencyTable()
        emit(Resource.Success(true))
    }
}
