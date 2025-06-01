package com.rcl.kduopass.presentation.navigation

import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.rcl.kduopass.di.IDIComponentContext
import com.rcl.kduopass.presentation.viewmodel.AddAccountStoreFactory

class AddAccountComponent(
    componentContext: IDIComponentContext
) : IDIComponentContext by componentContext {
    internal val addAccountStore by lazy {
        instanceKeeper.getStore {
            AddAccountStoreFactory(
                storeFactory = DefaultStoreFactory(),
                accountUseCases = appComponent.accountUseCases,
            ).create()
        }
    }
}