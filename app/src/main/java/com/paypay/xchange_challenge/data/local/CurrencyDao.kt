/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.data.local

import androidx.room.*

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyListing(currencyListingEntities: List<CurrencyListingEntity>)

    @Query("SELECT * FROM currency_listing")
    suspend fun getCurrencyListings(): List<CurrencyListingEntity>

    @Query("DELETE FROM currency_listing")
    suspend fun deleteCurrencyTable()

    @Query("UPDATE currency_listing SET rate = :rate WHERE symbol = :symbol")
    suspend fun updateCurrencyListing(symbol: String, rate: Double)

    @Transaction
    suspend fun updateCurrencyListingTx(currencies: List<Map<String, Double>>) {
        currencies.forEach { currency ->
            currency.forEach { (symbol, rate) ->
                updateCurrencyListing(symbol, rate)
            }
        }
    }
}