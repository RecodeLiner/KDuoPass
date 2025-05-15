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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.kduopass.domain.usecase.ThemeMode
import com.rcl.kduopass.presentation.navigation.RootComponent
import com.rcl.kduopass.presentation.viewmodel.SettingsViewModel
import kduopass.composeapp.generated.resources.Res
import kduopass.composeapp.generated.resources.about_title
import kduopass.composeapp.generated.resources.settings_export_accounts
import kduopass.composeapp.generated.resources.settings_import_accounts
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
    navigateBack: () -> Unit,
    navigateTo: (RootComponent.ScreenConfig) -> Unit
) {
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
                            contentDescription = "Navigate back from settings screen topappbar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        SettingsContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
            navigateToAbout = { navigateTo(RootComponent.ScreenConfig.About) }
        )
    }
}

@Composable
private fun SettingsContent(
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel,
    navigateToAbout: () -> Unit
) {
    val isExportInAction by viewModel.isInAction.collectAsState()
    val currentTheme by viewModel.currentTheme.collectAsState()

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
                enabled = !isExportInAction,
                onClick = viewModel::exportAccountsToFile
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