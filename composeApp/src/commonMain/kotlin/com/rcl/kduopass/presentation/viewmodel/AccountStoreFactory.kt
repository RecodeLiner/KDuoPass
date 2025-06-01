package com.rcl.kduopass.presentation.viewmodel

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.domain.usecase.AccountUseCases
import com.rcl.kduopass.presentation.viewmodel.components.AccountWithCode
import kotlinx.coroutines.launch

class AccountStoreFactory(
    private val storeFactory: StoreFactory,
    private val accountUseCases: AccountUseCases
) {
    fun create(): AccountStore =
        object : AccountStore, Store<AccountStore.Intent, AccountStore.State, Nothing> by storeFactory.create(
            name = "AccountStore",
            initialState = AccountStore.State(),
            bootstrapper = SimpleBootstrapper(Action.LoadAccounts),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object LoadAccounts : Action
    }

    private sealed interface Msg {
        data class AccountsLoaded(val accountsWithCode: List<AccountWithCode>) : Msg
        data object SetLoading : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<AccountStore.Intent, Action, AccountStore.State, Msg, Nothing>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.LoadAccounts -> loadAccounts()
            }
        }

        override fun executeIntent(intent: AccountStore.Intent) {
            when (intent) {
                is AccountStore.Intent.RefreshData -> loadAccounts()
                is AccountStore.Intent.DeleteAccount -> deleteAccount(intent.account)
            }
        }

        private fun loadAccounts() {
            dispatch(Msg.SetLoading)
            scope.launch {
                accountUseCases.getAccounts().collect { accountList ->
                    val accountsWithCode = accountList.map {
                        AccountWithCode(it, accountUseCases.generateCode(it.secret))
                    }
                    dispatch(Msg.AccountsLoaded(accountsWithCode))
                }
            }
        }

        private fun deleteAccount(account: AccountEntity) {
            scope.launch {
                accountUseCases.deleteAccount(account)
                loadAccounts()
            }
        }
    }

    private object ReducerImpl : Reducer<AccountStore.State, Msg> {
        override fun AccountStore.State.reduce(msg: Msg): AccountStore.State =
            when (msg) {
                is Msg.AccountsLoaded -> copy(accountsWithCode = msg.accountsWithCode, isLoading = false)
                is Msg.SetLoading -> copy(isLoading = true)
            }
    }
    interface AccountStore : Store<AccountStore.Intent, AccountStore.State, Nothing> {

        data class State(
            val accountsWithCode: List<AccountWithCode> = emptyList(),
            val isLoading: Boolean = false
        )

        sealed interface Intent {
            data object RefreshData : Intent
            data class DeleteAccount(val account: AccountEntity) : Intent
        }
    }
}