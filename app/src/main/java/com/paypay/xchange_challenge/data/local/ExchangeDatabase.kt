/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrencyListingEntity::class], version = 1, exportSchema = false)
abstract class ExchangeDatabase : RoomDatabase() {
    abstract val currencyDao: CurrencyDao
}