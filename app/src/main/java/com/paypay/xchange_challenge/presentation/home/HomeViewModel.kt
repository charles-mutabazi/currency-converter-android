/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paypay.xchange_challenge.domain.model.CurrencyListing
import com.paypay.xchange_challenge.domain.repository.ExchangeRepository
import com.paypay.xchange_challenge.util.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeViewModel(
    private val exchangeRepo: ExchangeRepository
) : ViewModel() {

    var selectedCurrency by mutableStateOf("USD")
    var getCurrentListing by mutableStateOf(CurrencyListState())
    var amount by mutableStateOf("") //<- amount to convert
    var convertedAmountList by mutableStateOf(getCurrentListing.currencies) //<- converted amount

    init {
        getExchangeRates()
    }

    fun getExchangeRates() {
        exchangeRepo.getCurrencyList().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    getCurrentListing = CurrencyListState(
                        isLoading = false,
                        currencies = result.data ?: emptyList()
                    )
                }

                is Resource.Error -> {
                    getCurrentListing = CurrencyListState(
                        isLoading = false,
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    getCurrentListing = CurrencyListState(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun convertCurrency() {
        //change string to double
        val amountToConvert = amount.toDoubleOrNull()
        amountToConvert?.let {
            //get the selected currency
            val selectedCurrency = getCurrentListing.currencies.find { currency ->
                currency.symbol == selectedCurrency
            }

            selectedCurrency?.let {
                val selectedInUSD = amountToConvert / selectedCurrency.rate

                //convert to other currencies
                val convertedList = getCurrentListing.currencies.map { currency ->
                    val convertedAmount = selectedInUSD * currency.rate
                    currency.copy(rate = convertedAmount)
                }
                convertedAmountList = convertedList
            }
        }

    }
}


data class CurrencyListState(
    val isLoading: Boolean = false,
    val currencies: List<CurrencyListing> = emptyList(),
    val error: String = ""
)