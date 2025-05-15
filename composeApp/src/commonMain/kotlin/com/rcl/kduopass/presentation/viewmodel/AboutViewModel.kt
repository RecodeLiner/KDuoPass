package com.rcl.kduopass.presentation.viewmodel

import com.rcl.kduopass.presentation.viewmodel.components.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import me.tatarka.inject.annotations.Inject

@Inject
class AboutViewModel: ViewModelComponent() {
    @Inject
    class AboutViewModelFactory : ViewModelFactory<AboutViewModel> {
        override fun create() = AboutViewModel()
    }

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    override fun onDestroy() {
        scope.cancel()
    }
}