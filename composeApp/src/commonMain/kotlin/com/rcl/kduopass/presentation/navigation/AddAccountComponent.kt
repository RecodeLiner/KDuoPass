package com.rcl.kduopass.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.rcl.kduopass.di.AppComponent

class AddAccountComponent(
    componentContext: ComponentContext,
    private val appComponent: AppComponent
) : ComponentContext by componentContext {
    val vm = instanceKeeper.getOrCreate { appComponent.addAccountViewModel() }
    init {
        lifecycle.doOnCreate { vm.onCreate() }
        lifecycle.doOnDestroy { vm.onDestroy() }
    }
}