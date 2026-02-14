package com.rcl.kduopass

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arkivanov.decompose.defaultComponentContext
import com.rcl.kduopass.data.database.AppDatabase
import com.rcl.kduopass.di.DIComponentContextImpl
import com.rcl.kduopass.di.prefs.DataStoreBuilder
import com.rcl.kduopass.di.prefs.DataStoreBuilder.Companion.DATA_STORE_FILE_NAME
import com.rcl.kduopass.presentation.navigation.RootComponent
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init


class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(this)
        val root =
            RootComponent(
                componentContext = DIComponentContextImpl(
                    defaultComponentContext(),
                    (application as App).appComponent()
                )
            )
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            @RequiresApi(Build.VERSION_CODES.S)
            @Composable
            fun dynamicColorScheme(ctx: Context) : ColorScheme {
                return if (isSystemInDarkTheme()) {
                    dynamicDarkColorScheme(ctx)
                } else {
                    dynamicLightColorScheme(ctx)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                App(
                    rootComponent = root,
                    colorScheme = dynamicColorScheme(this)
                )
            }
            else {
                App(rootComponent = root)
            }
        }
        if (intent.action == Intent.ACTION_APPLICATION_PREFERENCES) {
            root.navigateTo(RootComponent.ScreenConfig.Settings)
        }
    }
}

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("dpkss.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

fun getPreferencesDataStoreBuilder(ctx: Context): DataStoreBuilder {
    return DataStoreBuilder(ctx.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath)
}