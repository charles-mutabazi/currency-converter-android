package com.paypay.xchange_challenge.data.repository_impl

import androidx.room.Room
import com.paypay.xchange_challenge.FakeTestData.fakeCurrencies
import com.paypay.xchange_challenge.FakeTestData.fakeCurrencyListingEntities
import com.paypay.xchange_challenge.FakeTestData.fakeRateList
import com.paypay.xchange_challenge.FakeTestData.fakeRatesDTO
import com.paypay.xchange_challenge.FakeTestData.fakeRemoteCurrencyListing
import com.paypay.xchange_challenge.FakeTestData.mockClient
import com.paypay.xchange_challenge.data.local.ExchangeDatabase
import com.paypay.xchange_challenge.data.mapper.toCurrencyListing
import com.paypay.xchange_challenge.domain.repository.ExchangeRepository
import com.paypay.xchange_challenge.util.Resource
import io.ktor.client.*
import io.ktor.http.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeListingRepositoryImplTest : KoinTest {

    private lateinit var client: HttpClient
    private lateinit var repository: ExchangeListingRepositoryImpl

    @Before
    fun setup() {
        startKoin {
            androidContext(mockk())
            modules(module {
                single {
                    Room.inMemoryDatabaseBuilder(
                        androidApplication(),
                        ExchangeDatabase::class.java
                    ).build()
                }
                single<ExchangeRepository> {
                    ExchangeListingRepositoryImpl(get(), get())
                }
            })

        }
    }

    @After
    fun stopKoinAfterTest() = stopKoin()

    @Test
    fun `getLatestRates should return the expected result`() = runTest {
        client = mockClient(
            Json.encodeToString(fakeRatesDTO()),
            HttpStatusCode.OK
        )
        repository = ExchangeListingRepositoryImpl(mockk(), client)
        val expectedResult = fakeRateList()
        val result = repository.getLatestRates()

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getRemoteCurrencies should return the expected result`() = runTest {
        client = mockClient(
            Json.encodeToString(fakeCurrencies()),
            HttpStatusCode.OK
        )
        repository = ExchangeListingRepositoryImpl(mockk(), client)
        val expectedResult = fakeRemoteCurrencyListing()
        val result = repository.getRemoteCurrencies()

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getCurrencyList should return list from db when fetchFromRemote is false`() = runTest {
        // Given
        val expectedListings = fakeCurrencyListingEntities()
        val db = mockk<ExchangeDatabase>()

        coEvery { db.currencyDao.getCurrencyListings() } returns expectedListings
        val returnedDBListing = db.currencyDao.getCurrencyListings()


        // When
        repository = mockk()
            every { repository.getCurrencyList(fetchFromRemote = false) } returns flowOf(
                Resource.Success(
                    expectedListings.map { it.toCurrencyListing() })
            )
            val result = repository.getCurrencyList(fetchFromRemote = false).first()

            // Then
            assertEquals(returnedDBListing.map { it.toCurrencyListing() }, result.data)
            verify { repository.getCurrencyList(fetchFromRemote = false) }
            coVerify { db.currencyDao.getCurrencyListings() }
        }

    @Test
    fun `getCurrencyList should return list from api when fetchFromRemote is true`() = runTest {
        // Given
        val expectedListings = fakeCurrencyListingEntities()
        val db = mockk<ExchangeDatabase>()

        coEvery { db.currencyDao.insertCurrencyListing(any()) } returns Unit
        db.currencyDao.insertCurrencyListing(fakeCurrencyListingEntities())

        coEvery { db.currencyDao.updateCurrencyListingTx(any()) } returns Unit
        db.currencyDao.updateCurrencyListingTx(mockk())

            coEvery { db.currencyDao.getCurrencyListings() } returns expectedListings
            val returnedDBListing = db.currencyDao.getCurrencyListings()


            // When
            repository = mockk()
            every { repository.getCurrencyList(fetchFromRemote = false) } returns flowOf(
                Resource.Success(
                    expectedListings.map { it.toCurrencyListing() })
            )
            val result = repository.getCurrencyList(fetchFromRemote = false).first()

            // Then
            assertEquals(returnedDBListing.map { it.toCurrencyListing() }, result.data)

            coVerify { db.currencyDao.insertCurrencyListing(fakeCurrencyListingEntities()) }
            coVerify { db.currencyDao.updateCurrencyListingTx(any()) }
            coVerify { db.currencyDao.getCurrencyListings() }

        }
}


