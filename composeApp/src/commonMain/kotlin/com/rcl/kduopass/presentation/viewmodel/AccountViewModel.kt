package com.rcl.kduopass.presentation.viewmodel

import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.domain.usecase.AccountUseCases
import com.rcl.kduopass.presentation.viewmodel.components.AccountWithCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class AccountViewModel @Inject constructor(
    private val accountUseCases: AccountUseCases,
) : ViewModelComponent() {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _accounts = MutableStateFlow<List<AccountWithCode>>(emptyList())
    val accounts: StateFlow<List<AccountWithCode>> = _accounts.asStateFlow()

    override fun onCreate() {
        println("what")
        scope.launch {
            accountUseCases.getAccounts().collect { accountList ->
                print("check - ${accountList.joinToString(", ")}")
                _accounts.update {
                    accountList.map {
                        AccountWithCode(it, accountUseCases.generateCode(it.secret))
                    }
                }
            }
        }
    }

    override fun onResume() {
        scope.launch {
            refreshCodes()
        }
    }

    fun refreshCodes() {
        val currentAccounts = _accounts.value
        if (currentAccounts.isNotEmpty()) {
            _accounts.value = currentAccounts.map {
                AccountWithCode(it.account, accountUseCases.generateCode(it.account.secret))
            }
        }
    }

    fun deleteAccount(account: AccountEntity) {
        scope.launch {
            accountUseCases.deleteAccount(account)
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}