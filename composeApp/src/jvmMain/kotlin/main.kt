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
import com.rcl.kduopass.InternalBuildConfig
import com.rcl.kduopass.InternalBuildConfig.APP_NAME
import com.rcl.kduopass.data.database.AppDatabase
import com.rcl.kduopass.di.AppComponent
import com.rcl.kduopass.di.create
import com.rcl.kduopass.presentation.navigation.RootComponent
import io.github.vinceglb.filekit.FileKit
import java.awt.Dimension
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
fun main() {
    FileKit.init(appId = APP_NAME)
    val lifecycle = LifecycleRegistry()
    val appComponent = AppComponent::class.create(JvmPlatformComponent::class.create())

    val rootComponent = runOnUiThread {
        RootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            appComponent = appComponent,
        )
    }

    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = InternalBuildConfig.APP_NAME,
            content = {
                window.minimumSize = Dimension(350, 600)
                App(rootComponent)
            },
            onKeyEvent = {
                when (it.key) {
                    Key.Escape, Key.Backspace, Key.Back -> {
                        rootComponent.navigateBack()
                        true
                    }

                    else -> false
                }
            }
        )
    }
}

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "user.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}