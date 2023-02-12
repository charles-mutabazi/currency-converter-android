package com.paypay.xchange_challenge.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 12/02/2023
 */
@Serializable
data class RateDTO(
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)
