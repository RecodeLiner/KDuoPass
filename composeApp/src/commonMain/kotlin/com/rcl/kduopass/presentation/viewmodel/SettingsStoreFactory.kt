package com.rcl.kduopass.presentation.viewmodel

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.rcl.kduopass.domain.usecase.FileAccountsUseCase
import com.rcl.kduopass.domain.usecase.ThemeMode
import com.rcl.kduopass.domain.usecase.ThemeUseCase
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import kduopass.composeapp.generated.resources.Res
import kduopass.composeapp.generated.resources.import_select
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class SettingsStoreFactory(
    private val storeFactory: StoreFactory,
    private val themeUseCase: ThemeUseCase,
    private val fileAccountsUseCase: FileAccountsUseCase
) {
    fun create(): SettingsStore =
        object : SettingsStore,
            Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label> by storeFactory.create(
                name = "SettingsStore",
                initialState = SettingsStore.State(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
                bootstrapper = SimpleBootstrapper(Unit)
            ) { }

    private sealed interface Msg {
        data class IsInActionUpdated(val isInAction: Boolean) : Msg
        data class CurrentThemeUpdated(val themeMode: ThemeMode) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<SettingsStore.Intent, Unit, SettingsStore.State, Msg, SettingsStore.Label>() {
        override fun executeIntent(intent: SettingsStore.Intent) {
            when (intent) {
                is SettingsStore.Intent.ExportAccountsToFile -> exportAccountsToFile(
                    batchSize = intent.batchSize
                )

                is SettingsStore.Intent.UpdateTheme -> updateTheme(themeMode = intent.themeMode)

                is SettingsStore.Intent.ImportAccountsFromFile -> importAccountsFromFile(
                    isClean = intent.isClean,
                    batchSize = intent.batchSize
                )
            }
        }

        override fun executeAction(action: Unit) {
            scope.launch {
                dispatch(Msg.CurrentThemeUpdated(themeUseCase.currentThemeMode.first()))
            }
        }

        private fun exportAccountsToFile(batchSize: Int) {
            scope.launch {
                dispatch(Msg.IsInActionUpdated(true))
                val file = FileKit.openFileSaver(
                    suggestedName = "accounts",
                    extension = "json",
                )
                if (file == null) {
                    dispatch(Msg.IsInActionUpdated(false))
                    return@launch
                }

                fileAccountsUseCase.exportAccounts(file, batchSize)
                dispatch(Msg.IsInActionUpdated(false))
                publish(SettingsStore.Label.AccountsExported)
            }
        }

        private fun updateTheme(themeMode: ThemeMode) {
            scope.launch {
                themeUseCase.setThemeMode(themeMode)
                dispatch(Msg.CurrentThemeUpdated(themeMode))
            }
        }

        private fun importAccountsFromFile(isClean: Boolean, batchSize: Int) {
            scope.launch {
                dispatch(Msg.IsInActionUpdated(true))
                val file = FileKit.openFilePicker(
                    title = getString(Res.string.import_select), type = FileKitType.File(listOf("json"))
                )
                if (file == null) {
                    dispatch(Msg.IsInActionUpdated(false))
                    return@launch
                }

                fileAccountsUseCase.importAccounts(file, isClean, batchSize)
                dispatch(Msg.IsInActionUpdated(false))
                publish(SettingsStore.Label.AccountsImported)
            }
        }
    }

    private object ReducerImpl : Reducer<SettingsStore.State, Msg> {
        override fun SettingsStore.State.reduce(msg: Msg): SettingsStore.State =
            when (msg) {
                is Msg.IsInActionUpdated -> copy(isInAction = msg.isInAction)
                is Msg.CurrentThemeUpdated -> copy(currentTheme = msg.themeMode)
            }
    }

    interface SettingsStore :
        Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label> {

        data class State(
            val isInAction: Boolean = false,
            val currentTheme: ThemeMode = ThemeMode.NONE
        )

        sealed interface Intent {
            data class ExportAccountsToFile(val batchSize: Int = 100) : Intent
            data class UpdateTheme(val themeMode: ThemeMode) : Intent
            data class ImportAccountsFromFile(val isClean: Boolean = false, val batchSize: Int = 100) : Intent
        }

        sealed interface Label {
            object AccountsExported : Label
            object AccountsImported : Label
        }
    }
}