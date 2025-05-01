package com.rcl.kduopass.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.rcl.kduopass.data.database.AccountDao
import com.rcl.kduopass.data.database.AppDatabase
import com.rcl.kduopass.data.repository.AccountRepositoryImpl
import com.rcl.kduopass.domain.repository.AccountRepository
import com.rcl.kduopass.domain.usecase.AddAccountUseCase
import com.rcl.kduopass.domain.usecase.GenerateCodeUseCase
import com.rcl.kduopass.presentation.viewmodel.AccountViewModel
import com.rcl.kduopass.presentation.viewmodel.AddAccountViewModel
import com.rcl.kduopass.presentation.viewmodel.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Singleton
@Component
abstract class AppComponent(
    @Component val platformComponent: PlatformSpecificComponents
) {
    @Singleton
    @Provides
    fun accountRepository(impl: AccountRepositoryImpl): AccountRepository = impl
    @Singleton
    @Provides
    fun provideDataBase(builder: RoomDatabase.Builder<AppDatabase>) = builder
        .fallbackToDestructiveMigrationOnDowngrade(false)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

    @Singleton
    @Provides
    fun provideAccountDao(appDatabase: AppDatabase): AccountDao = appDatabase.accountDao()
    abstract val generateCodeUseCase: GenerateCodeUseCase
    abstract val addAccountUseCase: AddAccountUseCase
    abstract val accountFactory: AccountViewModel.AccountViewModelFactory
    abstract val addAccountFactory: AddAccountViewModel.AddAccountViewModelFactory
    abstract val settingsFactory: SettingsViewModel.SettingsViewModelFactory
}

