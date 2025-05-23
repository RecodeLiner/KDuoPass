package com.rcl.kduopass.presentation.viewmodel

import com.rcl.kduopass.domain.usecase.FileAccountsUseCase
import com.rcl.kduopass.presentation.viewmodel.components.ViewModelFactory
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import kduopass.composeapp.generated.resources.Res
import kduopass.composeapp.generated.resources.import_select
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.getString

class SettingsViewModel @Inject constructor(
    private val fileAccountsUseCase: FileAccountsUseCase
) : ViewModelComponent() {

    class SettingsViewModelFactory @Inject constructor(
        private val fileAccountsUseCase: FileAccountsUseCase
    ) : ViewModelFactory<SettingsViewModel> {
        override fun create() = SettingsViewModel(
            fileAccountsUseCase
        )
    }
    private val _isInAction = MutableStateFlow(false)
    val isInAction: StateFlow<Boolean> = _isInAction

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    fun exportAccountsToFile(batchSize: Int = 100) {
        scope.launch {
            _isInAction.value = true
            val file = FileKit.openFileSaver(
                suggestedName = "accounts",
                extension = "json",
            ) ?: return@launch

            fileAccountsUseCase.exportAccounts(file, batchSize)
            _isInAction.value = false
        }
    }


    fun importAccountsFromFile(isClean: Boolean = false, batchSize: Int = 100) {
        scope.launch {
            _isInAction.value = true
            val file = FileKit.openFilePicker(
                title = getString(Res.string.import_select),
                type = FileKitType.File(listOf("json"))
            ) ?: return@launch

            fileAccountsUseCase.importAccounts(file, isClean, batchSize)
            _isInAction.value = false
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}