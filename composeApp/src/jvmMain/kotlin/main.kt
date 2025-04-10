
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.rcl.kduopass.InternalBuildConfig
import com.rcl.kduopass.data.database.AppDatabase
import com.rcl.kduopass.di.AppComponent
import com.rcl.kduopass.di.create
import com.rcl.kduopass.presentation.navigation.RootComponent
import com.rcl.kduopass.presentation.screens.RootScreen
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.createDefaultTextStyle
import org.jetbrains.jewel.intui.standalone.theme.createEditorTextStyle
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.intui.window.styling.light
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.TitleBar
import org.jetbrains.jewel.window.newFullscreenControls
import org.jetbrains.jewel.window.styling.TitleBarStyle
import java.awt.Dimension
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
fun main() {
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
        val textStyle = JewelTheme.createDefaultTextStyle()
        val editorStyle = JewelTheme.createEditorTextStyle()

        val themeDefinition =
            if (isSystemInDarkTheme()) {
                JewelTheme.darkThemeDefinition(
                    defaultTextStyle = textStyle,
                    editorTextStyle = editorStyle
                )
            } else {
                JewelTheme.lightThemeDefinition(
                    defaultTextStyle = textStyle,
                    editorTextStyle = editorStyle
                )
            }

        IntUiTheme(
            theme = themeDefinition,
            styling =
                ComponentStyling.default()
                    .decoratedWindow(
                        titleBarStyle =
                            if (isSystemInDarkTheme()) {
                                TitleBarStyle.dark()
                            } else {
                                TitleBarStyle.light()
                            }
                    )
        ) {
            DecoratedWindow(
                onCloseRequest = ::exitApplication,
                state = windowState,
                title = InternalBuildConfig.APP_NAME,
                content = {
                    TitleBar(Modifier.newFullscreenControls()) {
                        Text(title)
                    }
                    window.minimumSize = Dimension(350, 600)
                    RootScreen(rootComponent)
                },
            )
        }
    }
}

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "user.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}