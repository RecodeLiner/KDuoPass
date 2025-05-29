package com.rcl.kduopass.presentation.navigation

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.rcl.kduopass.di.IDIComponentContext
import com.rcl.kduopass.presentation.navigation.RootComponent.ComponentChild.AboutChild
import com.rcl.kduopass.presentation.navigation.RootComponent.ComponentChild.AccountsChild
import com.rcl.kduopass.presentation.navigation.RootComponent.ComponentChild.AddAccountChild
import com.rcl.kduopass.presentation.navigation.RootComponent.ComponentChild.SettingsChild
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: IDIComponentContext
) : IDIComponentContext by componentContext {

    private val navigation = StackNavigation<ScreenConfig>()

    fun navigateTo(config: ScreenConfig) {
        navigation.pushToFront(config)
    }

    fun navigateBack() {
        navigation.pop()
    }

    val childStack = childStack(
        source = navigation,
        initialConfiguration = ScreenConfig.Accounts,
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = ScreenConfig.serializer()
    )

    private fun createChild(config: ScreenConfig, context: IDIComponentContext): ComponentChild {
        return when (config) {
            ScreenConfig.Accounts -> AccountsChild(AccountComponent(context))
            ScreenConfig.AddAccount -> AddAccountChild(
                AddAccountComponent(
                    context
                )
            )

            ScreenConfig.Settings -> SettingsChild(
                SettingsComponent(
                    context
                )
            )

            ScreenConfig.About -> AboutChild(AboutComponent(context))
        }
    }

    sealed class ComponentChild {
        data class AccountsChild(val components: AccountComponent) : ComponentChild()
        data class AddAccountChild(val components: AddAccountComponent) : ComponentChild()
        data class SettingsChild(val components: SettingsComponent) : ComponentChild()
        data class AboutChild(val component: AboutComponent) : ComponentChild()
    }

    @Serializable
    sealed interface ScreenConfig {
        @Serializable
        data object Accounts : ScreenConfig

        @Serializable
        data object AddAccount : ScreenConfig

        @Serializable
        data object Settings : ScreenConfig

        @Serializable
        data object About : ScreenConfig
    }
}
