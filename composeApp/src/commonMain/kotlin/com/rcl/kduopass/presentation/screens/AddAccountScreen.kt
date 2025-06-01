package com.rcl.kduopass.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rcl.kduopass.presentation.viewmodel.AddAccountStoreFactory
import com.rcl.kduopass.presentation.viewmodel.AddAccountStoreFactory.AddAccountStore.Intent.AddAccount
import kduopass.composeapp.generated.resources.Res
import kduopass.composeapp.generated.resources.add_account_screen_save
import kduopass.composeapp.generated.resources.add_account_screen_secret
import kduopass.composeapp.generated.resources.add_account_screen_service_name
import kduopass.composeapp.generated.resources.add_account_screen_title
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun AddAccountScreen(
    store: AddAccountStoreFactory.AddAccountStore,
    navigateBack: () -> Unit
) {
    val state by store.stateFlow.collectAsState()
    val labels by store.labels.collectAsState(null)
    var isInvalid by remember { mutableStateOf(false) }

    LaunchedEffect(labels) {
        when (val label = labels) {
            is AddAccountStoreFactory.AddAccountStore.Label.ValidationResult -> {
                if (label.isValid) {
                    store.accept(AddAccount(state))
                }
                else {
                    isInvalid = true
                    delay(2_000)
                    isInvalid = false
                }
            }
            AddAccountStoreFactory.AddAccountStore.Label.AccountAdded -> {
                navigateBack()
            }

            null -> {

            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.add_account_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = state.serviceName,
                onValueChange = {
                    store.accept(
                        AddAccountStoreFactory.AddAccountStore.Intent.UpdateServiceName(
                            it
                        )
                    )
                },
                label = { Text(stringResource(Res.string.add_account_screen_service_name)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(16.dp))

            TextField(
                isError = isInvalid,
                value = state.secret,
                onValueChange = {
                    store.accept(
                        AddAccountStoreFactory.AddAccountStore.Intent.UpdateSecret(
                            it
                        )
                    )
                },
                label = { Text(stringResource(Res.string.add_account_screen_secret)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(24.dp))

            Button(
                onClick = {
                    store.accept(AddAccountStoreFactory.AddAccountStore.Intent.ValidateSecret(state.secret))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.add_account_screen_save))
            }
        }
    }
}