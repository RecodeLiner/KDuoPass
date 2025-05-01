package com.rcl.kduopass.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.rcl.kduopass.presentation.viewmodel.AccountViewModel

class AccountComponent(
    context: ComponentContext,
    factory: AccountViewModel.AccountViewModelFactory
) : BaseComponent<AccountViewModel>(context, factory)