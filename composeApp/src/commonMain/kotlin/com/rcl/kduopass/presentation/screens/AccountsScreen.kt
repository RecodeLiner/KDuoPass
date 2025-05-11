package com.rcl.kduopass.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.kduopass.presentation.navigation.RootComponent
import com.rcl.kduopass.presentation.screens.components.AccountItem
import com.rcl.kduopass.presentation.viewmodel.AccountViewModel
import kduopass.composeapp.generated.resources.Res
import kduopass.composeapp.generated.resources.accounts_title
import kduopass.composeapp.generated.resources.add_account_screen_title
import kduopass.composeapp.generated.resources.empty_accounts_placeholder
import kduopass.composeapp.generated.resources.settings_title
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountListScreen(
    viewModel: AccountViewModel,
    navigateTo: (RootComponent.ScreenConfig) -> Unit
) {
    val accounts by viewModel.accounts.collectAsState()
    var secondsRemaining by remember { mutableStateOf(secondsUntilNextTick()) }

    LaunchedEffect(Unit) {
        while (true) {
            secondsRemaining = secondsUntilNextTick()
            if (secondsRemaining == 30) { // Начало нового 30-секундного окна
                viewModel.refreshCodes()
            }
            delay(1000L)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigateTo(RootComponent.ScreenConfig.AddAccount)
            }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(Res.string.add_account_screen_title))
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(Res.string.accounts_title))
                },
                actions = {
                    IconButton(onClick = {
                        navigateTo(RootComponent.ScreenConfig.Settings)
                    }) {
                        Icon(Icons.Filled.Settings, contentDescription = stringResource(Res.string.settings_title))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (accounts.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(Res.string.empty_accounts_placeholder),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                AnimatedVisibility(
                    visible = accounts.isNotEmpty(),
                    enter = fadeIn() + expandIn(),
                    exit = fadeOut() + shrinkOut()
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 220.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = accounts,
                            key = { it.account.id }
                        ) { accountWithCode ->
                            AccountItem(
                                accountWithCode = accountWithCode,
                                secondsRemaining = secondsRemaining,
                                onDelete = { viewModel.deleteAccount(accountWithCode.account) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
private fun secondsUntilNextTick(): Int {
    val now = Clock.System.now().toEpochMilliseconds()
    return 30 - ((now / 1000) % 30).toInt()
}