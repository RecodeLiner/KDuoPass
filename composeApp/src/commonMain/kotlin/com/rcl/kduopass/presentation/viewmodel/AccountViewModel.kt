package com.rcl.kduopass.presentation.viewmodel

import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.domain.repository.AccountRepository
import com.rcl.kduopass.domain.usecase.DeleteAccountUseCase
import com.rcl.kduopass.domain.usecase.GenerateCodeUseCase
import com.rcl.kduopass.presentation.viewmodel.components.AccountWithCode
import com.rcl.kduopass.presentation.viewmodel.components.ViewModelFactory
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
) : ViewModelComponent() {
    class AccountViewModelFactory @Inject constructor(
        private val accountRepository: AccountRepository,
        private val generateTOTP: GenerateCodeUseCase,
        private val deleteAccount: DeleteAccountUseCase
    ) : ViewModelFactory<AccountViewModel> {
        override fun create() =
            AccountViewModel(
                accountRepository,
                generateTOTP,
                deleteAccount
            )
    }

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _accounts = MutableStateFlow<List<AccountWithCode>>(emptyList())
    val accounts: StateFlow<List<AccountWithCode>> = _accounts.asStateFlow()

    override fun onCreate() {
        scope.launch {
            accountRepository.getAccounts().collect { accountList ->
                _accounts.value = accountList.map {
                    AccountWithCode(it, generateTOTP(it.secret))
                }
            }
        }
    }

    fun refreshCodes() {
        val currentAccounts = _accounts.value
        if (currentAccounts.isNotEmpty()) {
            _accounts.value = currentAccounts.map {
                AccountWithCode(it.account, generateTOTP(it.account.secret))
            }
        }
    }

    fun deleteAccount(account: AccountEntity) {
        scope.launch {
            deleteAccount.invoke(account)
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}