package com.rcl.kduopass.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.rcl.kduopass.data.database.AccountDao
import com.rcl.kduopass.data.database.AppDatabase
import com.rcl.kduopass.data.repository.AccountRepositoryImpl
import com.rcl.kduopass.di.prefs.DataStoreBuilder
import com.rcl.kduopass.domain.repository.AccountRepository
import com.rcl.kduopass.domain.usecase.AccountUseCases
import com.rcl.kduopass.domain.usecase.FileAccountsUseCase
import com.rcl.kduopass.domain.usecase.ThemeUseCase
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
    @Singleton
    @Provides
    fun provideDataStore(dataStoreBuilder: DataStoreBuilder) : DataStore<Preferences> =
        dataStoreBuilder.build()
    abstract val accountUseCases: AccountUseCases
    abstract val fileAccountsUseCase: FileAccountsUseCase
    abstract val themeUseCase: ThemeUseCase
}

