package com.rcl.kduopass.di

import androidx.room.RoomDatabase
import com.rcl.kduopass.data.database.AppDatabase

abstract class PlatformSpecificComponents {
    abstract val roomDatabase: RoomDatabase.Builder<AppDatabase>
}