package com.rcl.kduopass.presentation.screens.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rcl.kduopass.presentation.viewmodel.AccountWithCode
import kotlinx.coroutines.launch

@Composable
fun AccountItem(
    accountWithCode: AccountWithCode,
    secondsRemaining: Int,
    onDelete: () -> Unit
) {
    var showSecret by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val currentSecondsRemaining by rememberUpdatedState(newValue = secondsRemaining)
    val progress = (30 - currentSecondsRemaining) / 30f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "animatedProgress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .width(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = accountWithCode.account.serviceName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete account")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TOTPProgressIndicator(
                    progress = animatedProgress,
                    code = accountWithCode.code,
                    modifier = Modifier.size(100.dp)
                )

                Column {
                    Text(
                        text = if (showSecret) accountWithCode.account.secret else "•••••",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .clickable { showSecret = !showSecret }
                            .padding(vertical = 4.dp)
                    )

                    CopyTextButton(label = "Copy Code") {
                        clipboardManager.setText(AnnotatedString(accountWithCode.code))
                        scope.launch { snackbarHostState.showSnackbar("Code copied") }
                    }

                    CopyTextButton(label = "Copy Secret") {
                        clipboardManager.setText(AnnotatedString(accountWithCode.account.secret))
                        scope.launch { snackbarHostState.showSnackbar("Secret copied") }
                    }
                }
            }
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}

@Composable
private fun CopyTextButton(label: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text = label)
    }
}