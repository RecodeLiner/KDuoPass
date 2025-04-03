import androidx.compose.ui.window.ComposeUIViewController
import com.rcl.kduopass.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
