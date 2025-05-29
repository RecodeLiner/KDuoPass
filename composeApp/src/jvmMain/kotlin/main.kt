
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.rcl.kduopass.App
import com.rcl.kduopass.InternalBuildConfig.APP_NAME
import com.rcl.kduopass.data.database.AppDatabase
import com.rcl.kduopass.di.AppComponent
import com.rcl.kduopass.di.create
import com.rcl.kduopass.di.prefs.DataStoreBuilder
import com.rcl.kduopass.di.prefs.DataStoreBuilder.Companion.DATA_STORE_FILE_NAME
import com.rcl.kduopass.presentation.navigation.RootComponent
import com.rcl.kduopass.di.DIComponentContextImpl
import io.github.vinceglb.filekit.FileKit
import java.awt.Dimension
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@OptIn(ExperimentalLayoutApi::class)
fun main() {
    FileKit.init(appId = APP_NAME)
    val lifecycle = LifecycleRegistry()
    val appComponent = AppComponent::class.create(JvmPlatformComponent::class.create())

    val rootComponent = runOnUiThread {
        RootComponent(
            componentContext = DIComponentContextImpl(
                DefaultComponentContext(lifecycle = lifecycle),
                appComponent
            )
        )
    }

    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = APP_NAME,
            content = {
                window.minimumSize = Dimension(350, 600)
                App(rootComponent)
            },
            onKeyEvent = {
                when (it.key) {
                    Key.Escape, Key.Back -> {
                        rootComponent.navigateBack()
                        true
                    }

                    else -> false
                }
            }
        )
    }
}

private fun getDataDirPath(): String {
    val appDataDir = when (System.getProperty("os.name").lowercase()) {
        in listOf("linux", "unix") -> {
            Paths.get(System.getProperty("user.home"), ".config", APP_NAME).toString()
        }
        in listOf("mac os x", "darwin") -> {
            Paths.get(System.getProperty("user.home"), "Library", "Application Support", APP_NAME).toString()
        }
        else -> {
            Paths.get(System.getenv("APPDATA") ?: System.getProperty("user.home"), APP_NAME).toString()
        }
    }

    Files.createDirectories(Paths.get(appDataDir))

    return appDataDir
}

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(getDataDirPath(), "app_database.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}

fun getPreferencesDataStoreBuilder(): DataStoreBuilder {
    val prFile = File(getDataDirPath(), DATA_STORE_FILE_NAME)
    return DataStoreBuilder(prFile.absolutePath)
}