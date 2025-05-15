package com.rcl.kduopass.di

import androidx.room.RoomDatabase
import com.rcl.kduopass.data.database.AppDatabase
import com.rcl.kduopass.di.prefs.DataStoreBuilder

abstract class PlatformSpecificComponents {
    abstract val roomDatabase: RoomDatabase.Builder<AppDatabase>
    abstract val dataStorePrefsBuilder: DataStoreBuilder
}