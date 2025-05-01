package com.rcl.kduopass.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.rcl.kduopass.di.AppComponent
import com.rcl.kduopass.presentation.navigation.RootComponent.ComponentChild.AccountsChild
import com.rcl.kduopass.presentation.navigation.RootComponent.ComponentChild.AddAccountChild
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext,
    private val appComponent: AppComponent,
) : ComponentContext by componentContext {

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

    private fun createChild(config: ScreenConfig, context: ComponentContext): ComponentChild {
        return when (config) {
            ScreenConfig.Accounts -> AccountsChild(AccountComponent(context, appComponent.accountFactory))
            ScreenConfig.AddAccount -> AddAccountChild(AddAccountComponent(context, appComponent.addAccountFactory))
            ScreenConfig.Settings -> ComponentChild.SettingsChild(SettingsComponent(context, appComponent.settingsFactory))
        }
    }

    sealed class ComponentChild {
        data class AccountsChild(val components: AccountComponent) : ComponentChild()
        data class AddAccountChild(val components: AddAccountComponent) : ComponentChild()
        data class SettingsChild(val components: SettingsComponent) : ComponentChild()
    }
    @Serializable
    sealed interface ScreenConfig {
        @Serializable
        data object Accounts : ScreenConfig

        @Serializable
        data object AddAccount : ScreenConfig
        @Serializable
        data object Settings : ScreenConfig
    }
}
