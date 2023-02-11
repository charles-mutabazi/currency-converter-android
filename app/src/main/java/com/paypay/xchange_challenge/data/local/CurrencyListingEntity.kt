package com.paypay.xchange_challenge.data.local

import androidx.room.Entity

/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */

@Entity(tableName = "currency_listing", primaryKeys = ["symbol"])
data class CurrencyListingEntity(
    val name: String,
    val symbol: String,
    val rate: Double
)
