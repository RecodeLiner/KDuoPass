package com.rcl.kduopass.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.rcl.kduopass.presentation.viewmodel.SettingsViewModel

class SettingsComponent(
    componentContext: ComponentContext,
    factory: SettingsViewModel.SettingsViewModelFactory
) : BaseComponent<SettingsViewModel>(componentContext, factory)