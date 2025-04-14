package com.rcl.kduopass.presentation.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rcl.kduopass.presentation.viewmodel.AccountWithCode

@Composable
fun AccountItem(
    accountWithCode: AccountWithCode,
    progress: Float,
    onDelete: () -> Unit
) {
    var showSecret by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = accountWithCode.account.serviceName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = { onDelete() }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete account")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                TOTPProgressIndicator(
                    progress = progress,
                    code = accountWithCode.code,
                    modifier = Modifier.padding(end = 12.dp)
                )

                Column {
                    Text(
                        text = if (showSecret) accountWithCode.account.secret else "•••••",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.clickable { showSecret = !showSecret }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    TextButton(onClick = {
                        clipboardManager.setText(AnnotatedString(accountWithCode.code))
                    }) {
                        Text("Copy Code")
                    }

                    TextButton(onClick = {
                        clipboardManager.setText(AnnotatedString(accountWithCode.account.secret))
                    }) {
                        Text("Copy Secret")
                    }
                }
            }
        }
    }
}