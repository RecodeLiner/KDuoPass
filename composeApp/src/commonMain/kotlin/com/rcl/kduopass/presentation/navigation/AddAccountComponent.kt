package com.rcl.kduopass.presentation.navigation

import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.rcl.kduopass.di.IDIComponentContext
import com.rcl.kduopass.presentation.viewmodel.AddAccountViewModel

class AddAccountComponent(
    componentContext: IDIComponentContext
) : DIComponentWithLCBind<AddAccountViewModel>(componentContext) {
    override fun createViewModel(): AddAccountViewModel {
        return instanceKeeper.getOrCreate {
            AddAccountViewModel(appComponent.accountUseCases)
        }
    }
}