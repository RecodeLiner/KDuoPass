package com.rcl.kduopass.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.kduopass.presentation.navigation.RootComponent

@Composable
fun RootScreen(rootComponent: RootComponent) {
    val stack by rootComponent.childStack.subscribeAsState()

    Children(stack) {
        when (val child = it.instance) {
            is RootComponent.ComponentChild.AccountsChild -> AccountListScreen(child.components.vm)
            is RootComponent.ComponentChild.AddAccountChild -> AddAccountScreen(child.components.vm)
        }
    }

}