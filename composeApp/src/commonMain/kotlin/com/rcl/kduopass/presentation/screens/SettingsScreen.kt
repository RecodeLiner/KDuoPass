package com.rcl.kduopass.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.rcl.kduopass.domain.usecase.ThemeMode
import com.rcl.kduopass.presentation.navigation.RootComponent
import com.rcl.kduopass.presentation.viewmodel.SettingsStoreFactory
import kduopass.composeapp.generated.resources.Res
import kduopass.composeapp.generated.resources.about_title
import kduopass.composeapp.generated.resources.accounts_exported_success
import kduopass.composeapp.generated.resources.accounts_imported_success
import kduopass.composeapp.generated.resources.settings_export_accounts
import kduopass.composeapp.generated.resources.settings_import_accounts
import kduopass.composeapp.generated.resources.settings_import_accounts_append
import kduopass.composeapp.generated.resources.settings_import_accounts_clean
import kduopass.composeapp.generated.resources.settings_theme_dark
import kduopass.composeapp.generated.resources.settings_theme_light
import kduopass.composeapp.generated.resources.settings_theme_system
import kduopass.composeapp.generated.resources.settings_theme_title
import kduopass.composeapp.generated.resources.settings_title
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun SettingsScreen(
    store: SettingsStoreFactory.SettingsStore,
    navigateBack: () -> Unit,
    navigateTo: (RootComponent.ScreenConfig) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by store.stateFlow.collectAsState()
    val label by store.labels.collectAsState(null)

    LaunchedEffect(label) {
        when (label) {
            SettingsStoreFactory.SettingsStore.Label.AccountsExported -> {
                snackbarHostState.showSnackbar(
                    message = getString(Res.string.accounts_exported_success)
                )
            }
            SettingsStoreFactory.SettingsStore.Label.AccountsImported -> {
                snackbarHostState.showSnackbar(
                    message = getString(Res.string.accounts_imported_success)
                )
            }

            null -> {

            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                            contentDescription = "Navigate back from settings screen topappbar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        SettingsContent(
            paddingValues = paddingValues,
            state = state,
            onUpdateTheme = { themeMode -> store.accept(SettingsStoreFactory.SettingsStore.Intent.UpdateTheme(themeMode)) },
            onExportAccounts = { store.accept(SettingsStoreFactory.SettingsStore.Intent.ExportAccountsToFile()) },
            onImportAccountsClean = { store.accept(SettingsStoreFactory.SettingsStore.Intent.ImportAccountsFromFile(isClean = true)) },
            onImportAccountsAppend = { store.accept(SettingsStoreFactory.SettingsStore.Intent.ImportAccountsFromFile(isClean = false)) },
            navigateToAbout = { navigateTo(RootComponent.ScreenConfig.About) }
        )
    }
}

@Composable
private fun SettingsContent(
    paddingValues: PaddingValues,
    state: SettingsStoreFactory.SettingsStore.State,
    onUpdateTheme: (ThemeMode) -> Unit,
    onExportAccounts: () -> Unit,
    onImportAccountsClean: () -> Unit,
    onImportAccountsAppend: () -> Unit,
    navigateToAbout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = stringResource(Res.string.settings_theme_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ThemeButton(
                    text = stringResource(Res.string.settings_theme_light),
                    onClick = { onUpdateTheme(ThemeMode.LIGHT) },
                    isSelected = state.currentTheme == ThemeMode.LIGHT
                )
                ThemeButton(
                    text = stringResource(Res.string.settings_theme_dark),
                    onClick = { onUpdateTheme(ThemeMode.DARK) },
                    isSelected = state.currentTheme == ThemeMode.DARK
                )
                ThemeButton(
                    text = stringResource(Res.string.settings_theme_system),
                    onClick = { onUpdateTheme(ThemeMode.NONE) },
                    isSelected = state.currentTheme == ThemeMode.NONE
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(
                text = stringResource(Res.string.settings_export_accounts),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = !state.isInAction,
                onClick = onExportAccounts
            ) {
                Text(stringResource(Res.string.settings_export_accounts))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(
                text = stringResource(Res.string.settings_import_accounts),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    enabled = !state.isInAction,
                    onClick = onImportAccountsClean
                ) {
                    Text(stringResource(Res.string.settings_import_accounts_clean))
                }
                Button(
                    enabled = !state.isInAction,
                    onClick = onImportAccountsAppend
                ) {
                    Text(stringResource(Res.string.settings_import_accounts_append))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            ListItem(
                headlineContent = { Text(stringResource(Res.string.about_title)) },
                leadingContent = {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = stringResource(Res.string.about_title)
                    )
                },
                modifier = Modifier
                    .clickable(onClick = navigateToAbout)
                    .padding(horizontal = 16.dp)
            )
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