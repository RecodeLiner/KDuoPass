package com.rcl.kduopass.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun AccountListScreen(
    viewModel: AccountViewModel,
    navigateTo: (RootComponent.ScreenConfig) -> Unit
) {
    val accounts by viewModel.accounts.collectAsState()
    var secondsRemaining by remember { mutableStateOf(secondsUntilNextTick()) }

    LaunchedEffect(key1 = true) {
        while (true) {
            secondsRemaining = secondsUntilNextTick()
            delay(1000L)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigateTo(RootComponent.ScreenConfig.AddAccount)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add new account")
            }
        }
    ) { paddingValues ->
        if (accounts.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //TODO: remove hardcode string
                Text("No accounts yet")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 200.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(12.dp),
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

@OptIn(ExperimentalTime::class)
private fun secondsUntilNextTick(): Int {
    val now = Clock.System.now().toEpochMilliseconds()
    return 30 - ((now / 1000) % 30).toInt()
}