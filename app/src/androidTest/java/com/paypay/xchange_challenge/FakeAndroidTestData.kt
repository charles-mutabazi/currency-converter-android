/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 13/02/2023
 */
package com.paypay.xchange_challenge

import com.paypay.xchange_challenge.data.local.CurrencyListingEntity

object FakeAndroidTestData {

    private fun fakeRates(): Map<String, Double> {
        return mapOf(
            "AED" to 3.672,
            "AFN" to 77.0,
            "ALL" to 102.0,
            "AMD" to 480.0,
            "ANG" to 1.79,
            "AOA" to 636.0,
            "ARS" to 94.0,
            "AUD" to 1.36,
        )
    }

    fun fakeRateList() = fakeRates().map {
        mapOf(it.key to it.value)
    }

    fun fakeCurrencyListingEntities() = listOf(
        CurrencyListingEntity("United States Dollar", "USD", 1.0),
        CurrencyListingEntity("Euro", "EUR", 0.82),
        CurrencyListingEntity("British Pound Sterling", "GBP", 0.72),
        CurrencyListingEntity("Japanese Yen", "JPY", 109.0),
        CurrencyListingEntity("Australian Dollar", "AUD", 1.36),
        CurrencyListingEntity("Canadian Dollar", "CAD", 1.25),
        CurrencyListingEntity("Swiss Franc", "CHF", 0.91),
    )
}