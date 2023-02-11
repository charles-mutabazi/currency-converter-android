/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.data.mapper

import com.paypay.xchange_challenge.data.local.CurrencyListingEntity
import com.paypay.xchange_challenge.domain.model.CurrencyListing

fun CurrencyListingEntity.toCurrencyListing() = CurrencyListing(
    name = name,
    symbol = symbol,
    rate = rate
)

fun CurrencyListing.toCurrencyListingEntity() = CurrencyListingEntity(
    name = name,
    symbol = symbol,
    rate = rate
)