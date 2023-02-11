/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {
    @GET("latest.json")
    suspend fun getExchangeRates(
        @Query("app_id") appId: String,
    ): ResponseBody

    @GET("currencies.json")
    suspend fun getCurrencies(): ResponseBody

    companion object {
        const val BASE_URL = "https://openexchangerates.org/api/"
        const val APP_ID = "f48e23dad6f14fc182fb5a63fd68d003"
    }
}