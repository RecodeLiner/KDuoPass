package com.rcl.kduopass.presentation.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rcl.kduopass.presentation.viewmodel.SettingsViewModel
import kduopass.composeapp.generated.resources.Res
import kduopass.composeapp.generated.resources.settings_export_accounts
import kduopass.composeapp.generated.resources.settings_import_accounts_append
import kduopass.composeapp.generated.resources.settings_import_accounts_clean
import kduopass.composeapp.generated.resources.settings_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigateBack: () -> Unit
) {
    val isInAction by viewModel.isInAction.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.settings_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return back from settings"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item(
                key = "exportAccountsToFile"
            ) {
                Button(
                    enabled = !isInAction,
                    onClick = { viewModel.exportAccountsToFile() }
                ) {
                    Text(stringResource(Res.string.settings_export_accounts))
                }
            }
            item(
                key = "importAccountsFromFile"
            ) {
                Row {
                    Button(
                        enabled = !isInAction,
                        onClick = { viewModel.importAccountsFromFile(isClean = true) }
                    ) {
                        Text(stringResource(Res.string.settings_import_accounts_clean))
                    }
                    Button(
                        enabled = !isInAction,
                        onClick = { viewModel.importAccountsFromFile(isClean = false) }
                    ) {
                        Text(stringResource(Res.string.settings_import_accounts_append))
                    }
                }
            }
        }
    }
}