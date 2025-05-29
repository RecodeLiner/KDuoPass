package com.rcl.kduopass.presentation.navigation

import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.rcl.kduopass.di.IDIComponentContext
import com.rcl.kduopass.presentation.viewmodel.ViewModelComponent

abstract class DIComponentWithLCBind<T : ViewModelComponent>(
    context: IDIComponentContext
) : IDIComponentContext by context {
    val vm: T by lazy {
        createViewModel()
    }

    protected abstract fun createViewModel(): T

    init {
        lifecycle.doOnCreate { vm.onCreate() }
        lifecycle.doOnStart { vm.onStart() }
        lifecycle.doOnResume { vm.onResume() }
        lifecycle.doOnPause { vm.onPause() }
        lifecycle.doOnStop { vm.onStop() }
        lifecycle.doOnDestroy { vm.onDestroy() }
    }
}
