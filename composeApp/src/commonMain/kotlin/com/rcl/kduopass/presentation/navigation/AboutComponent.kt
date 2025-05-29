package com.rcl.kduopass.presentation.navigation

import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.rcl.kduopass.di.IDIComponentContext
import com.rcl.kduopass.presentation.viewmodel.AboutViewModel

class AboutComponent(
    componentContext: IDIComponentContext
) : DIComponentWithLCBind<AboutViewModel>(componentContext) {
    override fun createViewModel(): AboutViewModel {
        return instanceKeeper.getOrCreate {
            AboutViewModel()
        }
    }
}