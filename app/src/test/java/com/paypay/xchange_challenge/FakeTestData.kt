/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 13/02/2023
 */
package com.paypay.xchange_challenge

import com.paypay.xchange_challenge.data.local.CurrencyListingEntity
import com.paypay.xchange_challenge.data.remote.dto.RateDTO
import com.paypay.xchange_challenge.domain.model.CurrencyListing
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


object FakeTestData {
    private val responseHeader =
        headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

    fun mockClient(json: String, status: HttpStatusCode) = HttpClient(MockEngine) {
        engine {
            addHandler {
                respond(json, status, responseHeader)
            }
        }
        expectSuccess = true
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    const val errorResponse =
        """
            {
              "error": true,
              "status": 401,
              "message": "invalid_app_id",
              "description": "Invalid App ID provided. Please sign up at https://openexchangerates.org/signup, or contact support@openexchangerates.org."
            }
        """

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

    fun fakeRatesDTO() = RateDTO(
        disclaimer = "disclaimer text",
        license = "https://currencylayer.com/privacy",
        timestamp = 1613251200,
        base = "USD",
        rates = fakeRates()
    )

    fun fakeRateList() = fakeRates().map {
        mapOf(it.key to it.value)
    }

    fun fakeCurrencies() = mapOf(
        "USD" to "United States Dollar",
        "EUR" to "Euro",
        "GBP" to "British Pound Sterling"
    )

    fun fakeRemoteCurrencyListing() = listOf(
        CurrencyListing("United States Dollar", "USD", 0.0),
        CurrencyListing("Euro", "EUR", 0.0),
        CurrencyListing("British Pound Sterling", "GBP", 0.0),
    )

    fun fakeCurrencyListing() = listOf(
        CurrencyListing("United States Dollar", "USD", 1.0),
        CurrencyListing("Euro", "EUR", 0.8),
        CurrencyListing("British Pound Sterling", "GBP", 0.7),
    )

    fun fakeCurrencyListingConverted() = listOf(
        CurrencyListing("United States Dollar", "USD", 125.0),
        CurrencyListing("Euro", "EUR", 100.0),
        CurrencyListing("British Pound Sterling", "GBP", 87.5),
    )

    fun fakeCurrencyListingEntities() = mutableListOf(
        CurrencyListingEntity("United States Dollar", "USD", 1.0),
        CurrencyListingEntity("Euro", "EUR", 0.82),
        CurrencyListingEntity("British Pound Sterling", "GBP", 0.72),
    )
}