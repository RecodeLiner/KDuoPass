package com.rcl.kduopass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.kduopass.presentation.navigation.RootComponent
import com.rcl.kduopass.presentation.screens.RootScreen
import com.rcl.kduopass.presentation.theme.AppTheme

@Composable
internal fun App(rootComponent: RootComponent) = AppTheme(
    themeUseCase = rootComponent.appComponent.themeUseCase
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        RootScreen(rootComponent)
    }
}
