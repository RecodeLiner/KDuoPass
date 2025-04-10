package com.rcl.kduopass.presentation.viewmodel

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.rcl.kduopass.domain.usecase.AddAccountUseCase
import me.tatarka.inject.annotations.Inject

class AddAccountViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase
): InstanceKeeper.Instance {
    fun onCreate() {}
    override fun onDestroy() {  }
}