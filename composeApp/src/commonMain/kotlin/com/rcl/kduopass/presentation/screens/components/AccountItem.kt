package com.rcl.kduopass.presentation.screens.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.presentation.viewmodel.components.AccountWithCode
import kduopass.composeapp.generated.resources.Res
import kduopass.composeapp.generated.resources.code_copied
import kduopass.composeapp.generated.resources.copy_code
import kduopass.composeapp.generated.resources.copy_secret
import kduopass.composeapp.generated.resources.delete_account
import kduopass.composeapp.generated.resources.secret_copied
import kduopass.composeapp.generated.resources.secret_label
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

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

    val progress = secondsRemaining.toFloat() / 30f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500),
        label = "animatedProgress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = accountWithCode.account.serviceName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(Res.string.delete_account)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TOTPProgressIndicator(
                    progress = animatedProgress,
                    code = accountWithCode.code,
                    modifier = Modifier.size(100.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(Res.string.secret_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Box(
                        modifier = Modifier
                            .clickable { showSecret = !showSecret }
                            .padding(vertical = 4.dp)
                    ) {
                        Crossfade(targetState = showSecret, label = "secretCrossfade") { visible ->
                            Text(
                                text = if (visible) accountWithCode.account.secret else "•••••",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    CopyTextButton(label = stringResource(Res.string.copy_code)) {
                        clipboardManager.setText(AnnotatedString(accountWithCode.code))
                        scope.launch {
                            snackbarHostState.showSnackbar(getString(Res.string.code_copied))
                        }
                    }

                    CopyTextButton(label = stringResource(Res.string.copy_secret)) {
                        clipboardManager.setText(AnnotatedString(accountWithCode.account.secret))
                        scope.launch {
                            snackbarHostState.showSnackbar(getString(Res.string.secret_copied))
                        }
                    }
                }
            }
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}

@Composable
private fun CopyTextButton(label: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        modifier = Modifier.heightIn(min = 32.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
fun AccountItemPreview() {
    val dummyAccount = AccountEntity(
        id = 0,
        serviceName = "Example Service",
        secret = "JBSWY3DPEHPK3PXP"
    )
    val dummyAccountWithCode = AccountWithCode(dummyAccount, "123456")

    Box(modifier = Modifier.size(300.dp)){
        AccountItem(
            accountWithCode = dummyAccountWithCode,
            secondsRemaining = 20,
            onDelete = {}
        )
    }
}