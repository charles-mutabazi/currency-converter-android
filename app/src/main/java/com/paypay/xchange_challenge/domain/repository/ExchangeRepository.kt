/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.domain.repository

import com.paypay.xchange_challenge.domain.model.CurrencyListing
import com.paypay.xchange_challenge.util.Resource
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
    fun getCurrencyList(fetchFromRemote: Boolean = false): Flow<Resource<List<CurrencyListing>>>
}