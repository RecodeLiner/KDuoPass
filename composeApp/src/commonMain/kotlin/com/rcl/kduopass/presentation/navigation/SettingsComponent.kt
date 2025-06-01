package com.rcl.kduopass.presentation.navigation

import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.rcl.kduopass.di.IDIComponentContext
import com.rcl.kduopass.presentation.viewmodel.SettingsStoreFactory

class SettingsComponent(
    componentContext: IDIComponentContext
) : IDIComponentContext by componentContext {
    internal val settingsStore by lazy {
        instanceKeeper.getStore {
            SettingsStoreFactory(
                storeFactory = DefaultStoreFactory(),
                themeUseCase = appComponent.themeUseCase,
                fileAccountsUseCase = appComponent.fileAccountsUseCase,
            ).create()
        }
    }
}