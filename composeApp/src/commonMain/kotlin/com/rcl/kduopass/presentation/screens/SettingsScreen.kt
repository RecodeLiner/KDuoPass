package com.rcl.kduopass.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.kduopass.domain.usecase.ThemeMode
import com.rcl.kduopass.presentation.viewmodel.SettingsViewModel
import kduopass.composeapp.generated.resources.Res
import kduopass.composeapp.generated.resources.settings_export_accounts
import kduopass.composeapp.generated.resources.settings_import_accounts_append
import kduopass.composeapp.generated.resources.settings_import_accounts_clean
import kduopass.composeapp.generated.resources.settings_theme_dark
import kduopass.composeapp.generated.resources.settings_theme_light
import kduopass.composeapp.generated.resources.settings_theme_system
import kduopass.composeapp.generated.resources.settings_theme_title
import kduopass.composeapp.generated.resources.settings_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigateBack: () -> Unit
) {
    val isExportInAction by viewModel.isInAction.collectAsState()
    val currentTheme by viewModel.currentTheme.collectAsState()
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
            item(key = "themeSelection") {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(Res.string.settings_theme_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ThemeButton(
                            text = stringResource(Res.string.settings_theme_light),
                            onClick = { viewModel.updateTheme(ThemeMode.LIGHT) },
                            isSelected = currentTheme == ThemeMode.LIGHT
                        )
                        ThemeButton(
                            text = stringResource(Res.string.settings_theme_dark),
                            onClick = { viewModel.updateTheme(ThemeMode.DARK) },
                            isSelected = currentTheme == ThemeMode.DARK
                        )
                        ThemeButton(
                            text = stringResource(Res.string.settings_theme_system),
                            onClick = { viewModel.updateTheme(ThemeMode.NONE) },
                            isSelected = currentTheme == ThemeMode.NONE
                        )
                    }
                }
            }

            item(
                key = "exportAccountsToFile"
            ) {
                Button(
                    enabled = !isExportInAction,
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
                        enabled = !isExportInAction,
                        onClick = { viewModel.importAccountsFromFile(isClean = true) }
                    ) {
                        Text(stringResource(Res.string.settings_import_accounts_clean))
                    }
                    Button(
                        enabled = !isExportInAction,
                        onClick = { viewModel.importAccountsFromFile(isClean = false) }
                    ) {
                        Text(stringResource(Res.string.settings_import_accounts_append))
                    }
                }
            }
        }
    }
}


@Composable
private fun ThemeButton(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    if (isSelected) {
        Button(onClick = onClick) {
            Text(text)
        }
    } else {
        OutlinedButton(onClick = onClick) {
            Text(text)
        }
    }
}