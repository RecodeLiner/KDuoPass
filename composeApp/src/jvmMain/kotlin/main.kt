import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.rcl.kduopass.App
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

@OptIn(ExperimentalLayoutApi::class)
fun main() {
    application {

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
                state = rememberWindowState(width = 800.dp, height = 600.dp),
                title = com.rcl.kduopass.InternalBuildConfig.APP_NAME,
                content = {
                    TitleBar(Modifier.newFullscreenControls()) {
                        Text(title)
                    }
                    window.minimumSize = Dimension(350, 600)
                    App()
                },
            )
        }
    }
}