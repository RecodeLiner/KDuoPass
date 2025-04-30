package com.rcl.kduopass.presentation.viewmodel

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.rcl.kduopass.domain.model.Account
import com.rcl.kduopass.domain.repository.AccountRepository
import com.rcl.kduopass.domain.usecase.DeleteAccountUseCase
import com.rcl.kduopass.domain.usecase.GenerateCodeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val generateTOTP: GenerateCodeUseCase,
    private val deleteAccount: DeleteAccountUseCase
) : InstanceKeeper.Instance {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _accounts = MutableStateFlow<List<AccountWithCode>>(emptyList())
    val accounts: StateFlow<List<AccountWithCode>> = _accounts.asStateFlow()

    fun onCreate() {
        scope.launch {
            observeAccounts()
        }
    }

    private suspend fun observeAccounts() {
        accountRepository.getAccounts().collect { accountList ->
            updateAccountCodes(accountList)
        }
    }

    private fun updateAccountCodes(accounts: List<Account>) {
        val updated = accounts.map {
            val code = generateTOTP(it.secret)
            AccountWithCode(it, code)
        }
        _accounts.value = updated
    }

    fun deleteAccount(account: Account) {
        scope.launch {
            deleteAccount.invoke(account)
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}