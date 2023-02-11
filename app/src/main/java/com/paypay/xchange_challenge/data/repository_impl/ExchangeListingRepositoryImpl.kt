/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.data.repository_impl

import com.paypay.xchange_challenge.data.local.ExchangeDatabase
import com.paypay.xchange_challenge.data.mapper.toCurrencyListing
import com.paypay.xchange_challenge.data.remote.ExchangeApi
import com.paypay.xchange_challenge.domain.model.CurrencyListing
import com.paypay.xchange_challenge.domain.repository.ExchangeRepository
import com.paypay.xchange_challenge.util.Resource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeListingRepositoryImpl @Inject constructor(
    private val db: ExchangeDatabase,
    private val client: HttpClient
): ExchangeRepository {
    private val dao = db.currencyDao
    override fun getCurrencyList(fetchFromRemote: Boolean): Flow<Resource<List<CurrencyListing>>> = flow {
        emit(Resource.Loading(true))
        val localListings = dao.getCurrencyListings()

        emit( Resource.Success(
            data = localListings.map { it.toCurrencyListing() }
        ))

        val shouldLoadFromCache = !fetchFromRemote && localListings.isNotEmpty()

        if (shouldLoadFromCache) {
            emit(Resource.Loading(false))
            return@flow
        }

        //Get from remote using Ktor
        try {
            val response = client.get(ExchangeApi.BASE_URL + "currencies.json").body<Map<String, String>>()
            val currencyListings = response.map {
                CurrencyListing(symbol = it.key, rate = 0.0, name = it.value)
            }
            emit(Resource.Success(currencyListings))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    override suspend fun getCurrencyRate(from: String, to: String): Double {
        TODO("Not yet implemented")
    }
}
