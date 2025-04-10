package com.rcl.kduopass.presentation.viewmodel

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.rcl.kduopass.domain.model.Account
import com.rcl.kduopass.domain.repository.AccountRepository
import com.rcl.kduopass.domain.usecase.GenerateCodeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val generateTOTP: GenerateCodeUseCase
) : InstanceKeeper.Instance {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _accounts = MutableStateFlow<List<AccountWithCode>>(emptyList())
    val accounts: StateFlow<List<AccountWithCode>> = _accounts.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(secondsUntilNextTick())
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    fun onCreate() {
        scope.launch {
            launch { observeAccounts() }
            launch { startTicker() }
        }
    }

    private suspend fun observeAccounts() {
        accountRepository.getAccounts().collect { accountList ->
            updateAccountCodes(accountList)
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun startTicker() {
        while (true) {
            val now = Clock.System.now().toEpochMilliseconds()
            val remaining = 30 - ((now / 1000) % 30).toInt()
            _remainingSeconds.value = remaining

            if (remaining == 30) {
                // обновим коды
                updateAccountCodes(_accounts.value.map { it.account })
            }

            delay(1000L)
        }
    }

    private fun updateAccountCodes(accounts: List<Account>) {
        val updated = accounts.map {
            val code = generateTOTP(it.secret)
            AccountWithCode(it, code)
        }
        _accounts.value = updated
    }

    @OptIn(ExperimentalTime::class)
    private fun secondsUntilNextTick(): Int {
        val now = Clock.System.now().toEpochMilliseconds()
        return 30 - ((now / 1000) % 30).toInt()
    }

    override fun onDestroy() {
        scope.cancel()
    }
}