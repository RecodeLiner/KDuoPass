package com.rcl.kduopass.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.rcl.kduopass.presentation.viewmodel.ViewModelComponent
import com.rcl.kduopass.presentation.viewmodel.components.ViewModelFactory

abstract class BaseComponent<VM : ViewModelComponent>(
    componentContext: ComponentContext,
    factory: ViewModelFactory<VM>
) : ComponentContext by componentContext {

    val vm: VM = instanceKeeper.getOrCreate(
        key = factory::class.qualifiedName ?: factory::class.toString(),
        factory = factory::create
    )

    init {
        bindLifecycle()
    }

    private fun bindLifecycle() {
        lifecycle.doOnCreate { vm.onCreate() }
        lifecycle.doOnDestroy { vm.onDestroy() }
        lifecycle.doOnStop { vm.onPause() }
        lifecycle.doOnStart { vm.onStart() }
        lifecycle.doOnResume { vm.onResume() }
        lifecycle.doOnPause { vm.onPause() }
    }
}