package com.rcl.kduopass.presentation.navigation

import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.rcl.kduopass.di.IDIComponentContext
import com.rcl.kduopass.presentation.viewmodel.SettingsViewModel

class SettingsComponent(
    componentContext: IDIComponentContext
) : DIComponentWithLCBind<SettingsViewModel>(componentContext) {

    override fun createViewModel(): SettingsViewModel {
        return instanceKeeper.getOrCreate {
            SettingsViewModel(
                appComponent.themeUseCase,
                appComponent.fileAccountsUseCase
            )
        }
    }
}