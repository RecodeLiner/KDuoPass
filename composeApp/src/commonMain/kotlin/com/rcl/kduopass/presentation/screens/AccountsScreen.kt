package com.rcl.kduopass.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigateTo(RootComponent.ScreenConfig.AddAccount)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add new account")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(accounts, key = { it.account.id }) { accountWithCode ->
                AccountItem(
                    accountWithCode = accountWithCode,
                    progress = (30 - secondsRemaining) / 30f,
                    onDelete = { viewModel.deleteAccount(accountWithCode.account) }
                )
            }
        }
    }
}