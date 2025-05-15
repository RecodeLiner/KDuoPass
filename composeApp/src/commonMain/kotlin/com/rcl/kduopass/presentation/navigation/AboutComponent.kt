package com.rcl.kduopass.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.rcl.kduopass.presentation.viewmodel.AboutViewModel

class AboutComponent(
    componentContext: ComponentContext,
    factory: AboutViewModel.AboutViewModelFactory
) : BaseComponent<AboutViewModel>(componentContext, factory)