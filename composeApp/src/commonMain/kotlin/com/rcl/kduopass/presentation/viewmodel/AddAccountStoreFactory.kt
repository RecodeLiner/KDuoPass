package com.rcl.kduopass.presentation.viewmodel

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.domain.usecase.AccountUseCases
import com.rcl.kduopass.utils.TOTPUtils
import kotlinx.coroutines.launch

class AddAccountStoreFactory(
    private val storeFactory: StoreFactory,
    private val accountUseCases: AccountUseCases
) {
    fun create(): AddAccountStore =
        object : AddAccountStore,
            Store<AddAccountStore.Intent, AddAccountStore.State, AddAccountStore.Label> by storeFactory.create(
                name = "AddAccountStore",
                initialState = AddAccountStore.State(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) { }

    private sealed interface Msg {
        data class StateUpdated(val serviceName: String? = null, val secret: String? = null) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<AddAccountStore.Intent, Unit, AddAccountStore.State, Msg, AddAccountStore.Label>() {
        override fun executeIntent(intent: AddAccountStore.Intent) {
            when (intent) {
                is AddAccountStore.Intent.UpdateServiceName -> {
                    dispatch(Msg.StateUpdated(serviceName = intent.serviceName))
                }

                is AddAccountStore.Intent.UpdateSecret -> {
                    dispatch(Msg.StateUpdated(secret = intent.secret))
                }

                is AddAccountStore.Intent.AddAccount -> {
                    scope.launch {
                        val account = AccountEntity(
                            id = 0,
                            serviceName = intent.state.serviceName,
                            secret = intent.state.secret
                        )
                        accountUseCases.addAccount(account)
                        publish(AddAccountStore.Label.AccountAdded)
                    }
                }

                is AddAccountStore.Intent.ValidateSecret -> {
                    dispatch(Msg.StateUpdated(secret = intent.secret))
                    val isValid = try {
                        TOTPUtils.generateTOTP(intent.secret)
                        true
                    } catch (_: Exception) {
                        false
                    }
                    publish(AddAccountStore.Label.ValidationResult(isValid))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<AddAccountStore.State, Msg> {
        override fun AddAccountStore.State.reduce(msg: Msg): AddAccountStore.State =
            when (msg) {
                is Msg.StateUpdated -> copy(
                    serviceName = msg.serviceName ?: serviceName,
                    secret = msg.secret ?: secret
                )
            }
    }

    interface AddAccountStore : Store<AddAccountStore.Intent, AddAccountStore.State, AddAccountStore.Label> {

        data class State(
            val serviceName: String = "",
            val secret: String = ""
        )

        sealed interface Intent {
            data class UpdateServiceName(val serviceName: String) : Intent
            data class UpdateSecret(val secret: String) : Intent
            data class AddAccount(val state: State) : Intent
            data class ValidateSecret(val secret:String) : Intent
        }
        sealed interface Label {
            data class ValidationResult(val isValid: Boolean) : Label
            object AccountAdded : Label
        }
    }
}