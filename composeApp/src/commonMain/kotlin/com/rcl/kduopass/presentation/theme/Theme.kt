package com.rcl.kduopass.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.rcl.kduopass.domain.usecase.ThemeMode
import com.rcl.kduopass.domain.usecase.ThemeUseCase

@Composable
internal fun AppTheme(
    themeUseCase: ThemeUseCase,
    content: @Composable () -> Unit
) {
    val theme = themeUseCase.currentThemeMode.collectAsState(ThemeMode.NONE)
    val isDark = when (theme.value) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.NONE -> isSystemInDarkTheme()
    }

    MaterialTheme(
        colorScheme = if (isDark) darkColorScheme() else lightColorScheme(),
        content = { Surface(content = content) }
    )
}
