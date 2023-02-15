package com.paypay.xchange_challenge.presentation.home

import com.paypay.xchange_challenge.FakeTestData.fakeCurrencyListing
import com.paypay.xchange_challenge.FakeTestData.fakeCurrencyListingConverted
import com.paypay.xchange_challenge.domain.repository.ExchangeRepository
import com.paypay.xchange_challenge.util.Resource
import io.ktor.client.*
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `viewModel should use correct repository`() {
        val exchangeRepo: ExchangeRepository = mockk(relaxed = true)
        val homeViewModel = HomeViewModel(exchangeRepo)
        homeViewModel.getExchangeRates()
        verify { exchangeRepo.getCurrencyList() }
    }

    @Test
    fun `convertCurrency should return expected result`() = runTest {
        val exchangeRepo: ExchangeRepository = mockk(relaxed = true)
        val homeViewModel = HomeViewModel(exchangeRepo)

        every { exchangeRepo.getCurrencyList() } returns flowOf(Resource.Success(fakeCurrencyListing()))

        homeViewModel.amount = "100"
        homeViewModel.selectedCurrency = "EUR"
        homeViewModel.getCurrentListing = CurrencyListState(currencies = fakeCurrencyListing())

        // Call the function to be tested
        homeViewModel.convertCurrency()

        // Verify the output
        val expectedList = fakeCurrencyListingConverted()
        assertEquals(expectedList, homeViewModel.convertedAmountList)
        verify { exchangeRepo.getCurrencyList() }
    }

    @Test
    fun `getExchangeRates should return expected result`(): Unit = runTest {
        val exchangeRepo: ExchangeRepository = mockk(relaxed = true)
        val homeViewModel = HomeViewModel(exchangeRepo)

        val expectedListData = CurrencyListState(currencies = fakeCurrencyListing())
        val expectedLoadingData = CurrencyListState(isLoading = true)
        val expectedErrorData = CurrencyListState(error = "Something went wrong")

        every { exchangeRepo.getCurrencyList() } answers { res ->
            flowOf(
                Resource.Loading(true),
                Resource.Success(fakeCurrencyListing()),
                Resource.Error("Something went wrong"),
            )
        }

        val result = exchangeRepo.getCurrencyList().toList()
        val resultToState = result.map {
            when (it) {
                is Resource.Loading -> CurrencyListState(isLoading = true)
                is Resource.Success -> CurrencyListState(currencies = it.data!!)
                is Resource.Error -> CurrencyListState(error = it.message!!)
            }
        }

        // Call the function to be tested
        homeViewModel.getExchangeRates()

        assertEquals(
            listOf(
                expectedLoadingData,
                expectedListData,
                expectedErrorData
            ), resultToState
        )

        // Verify the output
        verify { exchangeRepo.getCurrencyList() }
        verify { homeViewModel.getExchangeRates() }
    }
}
