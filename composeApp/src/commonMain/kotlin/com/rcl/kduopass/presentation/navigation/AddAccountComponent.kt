package com.rcl.kduopass.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.rcl.kduopass.presentation.viewmodel.AddAccountViewModel

class AddAccountComponent(
    componentContext: ComponentContext,
    factory: AddAccountViewModel.AddAccountViewModelFactory
) : BaseComponent<AddAccountViewModel>(componentContext, factory)