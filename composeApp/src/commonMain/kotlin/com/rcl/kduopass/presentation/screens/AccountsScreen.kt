package com.rcl.kduopass.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rcl.kduopass.presentation.screens.components.TOTPProgressIndicator
import com.rcl.kduopass.presentation.viewmodel.AccountViewModel

@Composable
fun AccountListScreen(viewModel: AccountViewModel) {
    val accounts by viewModel.accounts.collectAsState()
    val secondsRemaining by viewModel.remainingSeconds.collectAsState()
    val progress = (30 - secondsRemaining) / 30f

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(accounts) { accountWithCode ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TOTPProgressIndicator(
                    progress = progress,
                    code = accountWithCode.code,
                    modifier = Modifier.padding(end = 12.dp)
                )

                Column {
                    Text(accountWithCode.account.serviceName, fontWeight = FontWeight.Medium)
                    Text("Secret: ${accountWithCode.account.secret}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
