package com.rcl.kduopass.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.rcl.kduopass.domain.usecase.ThemeMode
import com.rcl.kduopass.domain.usecase.ThemeUseCase

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AppTheme(
    colorScheme: ColorScheme?,
    themeUseCase: ThemeUseCase,
    content: @Composable () -> Unit
) {
    val theme = themeUseCase.currentThemeMode.collectAsState(ThemeMode.NONE)

    MaterialExpressiveTheme (
        colorScheme = when (theme.value) {
            ThemeMode.DARK -> darkColorScheme()
            ThemeMode.LIGHT -> lightColorScheme()
            ThemeMode.NONE -> colorScheme?: if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
        },
        content = { Surface(content = content) }
    )
}
