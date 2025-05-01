package com.rcl.kduopass.presentation.viewmodel

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle

abstract class ViewModelComponent: InstanceKeeper.Instance, Lifecycle.Callbacks {
    abstract override fun onDestroy()
}