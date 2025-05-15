package com.rcl.kduopass.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.kduopass.presentation.navigation.RootComponent

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootScreen(rootComponent: RootComponent) {
    val stack by rootComponent.childStack.subscribeAsState()

    ChildStack(
        stack = stack,
        animation = stackAnimation(
            animator = fade() + scale(),
            predictiveBackParams = {
                PredictiveBackParams(
                    backHandler = rootComponent.backHandler,
                    onBack = rootComponent::navigateBack,
                    animatable = ::androidPredictiveBackAnimatable,
                )
            }
        ),
    ) {
        when (val child = it.instance) {
            is RootComponent.ComponentChild.AccountsChild -> AccountListScreen(
                child.components.vm,
                rootComponent::navigateTo
            )

            is RootComponent.ComponentChild.AddAccountChild -> AddAccountScreen(
                child.components.vm,
                rootComponent::navigateBack
            )

            is RootComponent.ComponentChild.SettingsChild -> SettingsScreen(
                child.components.vm,
                rootComponent::navigateBack,
                rootComponent::navigateTo
            )

            is RootComponent.ComponentChild.AboutChild -> AboutScreen(
                child.component.vm,
                rootComponent::navigateBack
            )
        }
    }
}