package com.rcl.kduopass.presentation.navigation

import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.rcl.kduopass.di.IDIComponentContext
import com.rcl.kduopass.presentation.viewmodel.AccountStoreFactory

class AccountComponent(
    componentContext: IDIComponentContext,
) : IDIComponentContext by componentContext {
    internal val accountStore by lazy {
        instanceKeeper.getStore {
            AccountStoreFactory(
                storeFactory = DefaultStoreFactory(),
                accountUseCases = appComponent.accountUseCases
            ).create()
        }
    }
}