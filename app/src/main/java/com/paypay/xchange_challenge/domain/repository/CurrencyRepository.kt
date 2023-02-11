/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.domain.repository

import com.paypay.xchange_challenge.domain.model.CurrencyListing

interface CurrencyRepository {
    suspend fun getCurrencyList(): List<CurrencyListing>
    suspend fun getCurrencyRate(from: String, to: String): Double
}