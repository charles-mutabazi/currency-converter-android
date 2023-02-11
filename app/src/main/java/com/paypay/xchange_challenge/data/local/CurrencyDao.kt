/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyListing(currencyListingEntities: List<CurrencyListingEntity>)

    @Query("SELECT * FROM currency_listing")
    suspend fun getCurrencyListings(): List<CurrencyListingEntity>


}