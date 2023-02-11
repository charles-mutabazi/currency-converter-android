/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 11/02/2023
 */
package com.paypay.xchange_challenge.di

import com.paypay.xchange_challenge.data.repository_impl.ExchangeListingRepositoryImpl
import com.paypay.xchange_challenge.domain.repository.ExchangeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideExchangeRepository(
        exchangeListingRepositoryImpl: ExchangeListingRepositoryImpl
    ): ExchangeRepository
}