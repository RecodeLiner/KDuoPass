package com.rcl.kduopass.presentation.viewmodel

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.rcl.kduopass.domain.model.Account
import com.rcl.kduopass.domain.usecase.AddAccountUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class AddAccountViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase
) : InstanceKeeper.Instance {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun addAccount(account: Account, onAccountAdded: () -> Unit) {
        scope.launch {
            addAccountUseCase(account)
            onAccountAdded()
        }
    }

    fun onCreate() {}

    override fun onDestroy() {
        scope.cancel()
    }
}