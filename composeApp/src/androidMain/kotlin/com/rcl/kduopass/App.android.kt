package com.rcl.kduopass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arkivanov.decompose.defaultComponentContext
import com.rcl.kduopass.data.database.AppDatabase
import com.rcl.kduopass.presentation.navigation.RootComponent


class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root =
            RootComponent(
                componentContext = defaultComponentContext(),
                appComponent = (application as App).appComponent(),
            )
        enableEdgeToEdge()
        setContent {
            App(root)
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
