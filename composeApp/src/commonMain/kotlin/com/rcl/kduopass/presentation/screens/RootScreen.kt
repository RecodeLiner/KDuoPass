package com.rcl.kduopass.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.kduopass.presentation.navigation.RootComponent

@Composable
fun RootScreen(rootComponent: RootComponent) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)){
            val stack by rootComponent.childStack.subscribeAsState()

            Children(stack) {
                when (val child = it.instance) {
                    is RootComponent.ComponentChild.AccountsChild -> AccountListScreen(
                        child.components.vm,
                        rootComponent::navigateTo
                    )

                    is RootComponent.ComponentChild.AddAccountChild -> AddAccountScreen(
                        child.components.vm,
                        rootComponent::navigateBack
                    )
                }
            }
        }
    }
}