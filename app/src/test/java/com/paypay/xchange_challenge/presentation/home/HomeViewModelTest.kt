package com.paypay.xchange_challenge.presentation.home

import com.paypay.xchange_challenge.FakeTestData.fakeCurrencyListing
import com.paypay.xchange_challenge.FakeTestData.fakeCurrencyListingConverted
import com.paypay.xchange_challenge.domain.repository.ExchangeRepository
import com.paypay.xchange_challenge.util.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
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
    fun convertCurrencyTest(): Unit = runBlocking {
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
    }
}
