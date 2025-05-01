package com.rcl.kduopass.presentation.viewmodel.components

import com.rcl.kduopass.presentation.viewmodel.ViewModelComponent

interface ViewModelFactory<T: ViewModelComponent> {
    fun create(): T
}