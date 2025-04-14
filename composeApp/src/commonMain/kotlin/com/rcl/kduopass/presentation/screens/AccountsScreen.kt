package com.rcl.kduopass.presentation.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.kduopass.presentation.navigation.RootComponent
import com.rcl.kduopass.presentation.screens.components.AccountItem
import com.rcl.kduopass.presentation.viewmodel.AccountViewModel

@Composable
fun AccountListScreen(
    viewModel: AccountViewModel,
    navigateTo: (RootComponent.ScreenConfig) -> Unit
) {
    val accounts by viewModel.accounts.collectAsState()
    val secondsRemaining by viewModel.remainingSeconds.collectAsState()

    val progress = (30 - secondsRemaining) / 30f

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigateTo(RootComponent.ScreenConfig.AddAccount)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add new account")
            }
        }
    ) { paddingValues ->
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
                    progress = progress,
                    onDelete = { viewModel.deleteAccount(accountWithCode.account) }
                )
            }
        }
    }
}