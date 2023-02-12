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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val exchangeRepo: ExchangeRepository
) : ViewModel() {

    var selectedCurrency by mutableStateOf("USD")
    private var _getCurrentListing by mutableStateOf(CurrencyListState())
    val getCurrentListing get() = _getCurrentListing

    init {
        getExchangeRates()
    }

    private fun getExchangeRates() {
        exchangeRepo.getCurrencyList().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _getCurrentListing = CurrencyListState(
                        isLoading = false,
                        currencies = result.data ?: emptyList()
                    )
                }

                is Resource.Error -> {
                    _getCurrentListing = CurrencyListState(
                        isLoading = false,
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _getCurrentListing = CurrencyListState(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


    fun deleteCurrencyTable() {
        exchangeRepo.deleteCurrencyTable().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _getCurrentListing = CurrencyListState(
                        isLoading = false,
                        currencies = emptyList()
                    )
                }

                is Resource.Error -> {
                    _getCurrentListing = CurrencyListState(
                        isLoading = false,
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _getCurrentListing = CurrencyListState(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}



data class CurrencyListState(
    val isLoading: Boolean = false,
    val currencies: List<CurrencyListing> = emptyList(),
    val error: String = ""
)