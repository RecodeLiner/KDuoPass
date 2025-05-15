package com.rcl.kduopass.di

import android.content.Context
import androidx.room.RoomDatabase
import com.rcl.kduopass.data.database.AppDatabase
import com.rcl.kduopass.di.prefs.DataStoreBuilder
import com.rcl.kduopass.getDatabaseBuilder
import com.rcl.kduopass.getPreferencesDataStoreBuilder
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class AndroidPlatformComponent(
    private val applicationContext: Context
) : PlatformSpecificComponents() {
    val providesApplicationContext: Context
        @Provides get() = applicationContext
    @Provides
    fun providesDatabase(applicationContext: Context): RoomDatabase.Builder<AppDatabase> =
        getDatabaseBuilder(applicationContext)
    @Provides
    fun providesDataStore(applicationContext: Context): DataStoreBuilder =
        getPreferencesDataStoreBuilder(applicationContext)
}