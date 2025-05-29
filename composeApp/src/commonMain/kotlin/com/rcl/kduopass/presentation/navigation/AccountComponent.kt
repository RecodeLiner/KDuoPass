package com.rcl.kduopass.presentation.navigation

import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.rcl.kduopass.di.IDIComponentContext
import com.rcl.kduopass.presentation.viewmodel.AccountViewModel

class AccountComponent(
    componentContext: IDIComponentContext,
) : DIComponentWithLCBind<AccountViewModel>(componentContext) {
    override fun createViewModel(): AccountViewModel {
        return instanceKeeper.getOrCreate {
            AccountViewModel(appComponent.accountUseCases)
        }
    }
}