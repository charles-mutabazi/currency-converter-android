package com.paypay.xchange_challenge.domain.model

/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
data class ExchangeRateResponse(
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
    )
