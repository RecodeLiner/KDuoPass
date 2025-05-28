package com.rcl.kduopass.presentation.viewmodel

import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.domain.usecase.AddAccountUseCase
import com.rcl.kduopass.presentation.viewmodel.components.ViewModelFactory
import com.rcl.kduopass.utils.TOTPUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class AddAccountViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase
) : ViewModelComponent() {
    class AddAccountViewModelFactory @Inject constructor(
        private val addAccountUseCase: AddAccountUseCase
    ) : ViewModelFactory<AddAccountViewModel> {
        override fun create() = AddAccountViewModel(addAccountUseCase)
    }

    data class AddAccountState(
        val serviceName: String = "",
        val secret: String = ""
    )
    private val _state = MutableStateFlow(AddAccountState())
    val state : StateFlow<AddAccountState> = _state

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun addAccount(account: AccountEntity, onAccountAdded: () -> Unit) {
        scope.launch {
            addAccountUseCase(account)
            onAccountAdded()
        }
    }

    fun clearSecret() {
        _state.update {
            it.copy(secret = it.secret.trim().uppercase())
        }
    }

    fun validateSecret() : Boolean {
        clearSecret()
        return try {
            TOTPUtils.generateTOTP(_state.value.secret)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun updateState(secret: String? = null, serviceName: String? = null) {
        if (secret != null) _state.update { it.copy(secret = secret) }
        if (serviceName != null) _state.update { it.copy(serviceName = serviceName) }
    }

    override fun onCreate() {}

    override fun onDestroy() {
        scope.cancel()
    }
}