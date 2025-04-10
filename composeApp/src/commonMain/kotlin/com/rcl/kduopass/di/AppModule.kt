package com.rcl.kduopass.di

import com.rcl.kduopass.data.repository.AccountRepositoryImpl
import com.rcl.kduopass.domain.repository.AccountRepository
import com.rcl.kduopass.domain.usecase.AccountUseCases
import me.tatarka.inject.annotations.Inject

@Inject
abstract class AppModule {
    abstract fun provideAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository

    abstract fun provideUseCases(
        repository: AccountRepository
    ): AccountUseCases
}