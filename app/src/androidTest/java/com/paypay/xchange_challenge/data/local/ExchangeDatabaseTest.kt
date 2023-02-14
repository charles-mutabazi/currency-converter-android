package com.paypay.xchange_challenge.data.local

/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 14/02/2023
 */

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.paypay.xchange_challenge.FakeAndroidTestData.fakeCurrencyListingEntities
import com.paypay.xchange_challenge.FakeAndroidTestData.fakeRateList
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExchangeDatabaseTest : TestCase() {
    // get reference to the LanguageDatabase and LanguageDao class
    private lateinit var db: ExchangeDatabase
    private lateinit var dao: CurrencyDao
    private val currencies = fakeCurrencyListingEntities()

    // Override function setUp() and annotate it with @Before
    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ExchangeDatabase::class.java).build()
        dao = db.currencyDao
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertAndRetrieve() = runBlocking {
        dao.insertCurrencyListing(currencies)
        val result = dao.getCurrencyListings()
        assertEquals(currencies, result)
    }

    @Test
    fun updateCurrencyListing() = runBlocking {
        dao.insertCurrencyListing(currencies)
        dao.updateCurrencyListing("USD", 1.5)

        val updated = dao.getCurrencyListings().first()
        assertEquals(1.5, updated.rate)
    }

    @Test
    fun updateCurrencyListingTx() = runBlocking {
        dao.insertCurrencyListing(currencies)
        dao.updateCurrencyListingTx(fakeRateList())

        currencies.forEach { currency ->
            val updated = dao.getCurrencyListings().firstOrNull { it.symbol == currency.symbol }
            assertEquals(currency.rate, updated?.rate)
        }
    }

}