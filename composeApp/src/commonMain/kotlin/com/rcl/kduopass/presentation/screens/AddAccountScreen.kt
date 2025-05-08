package com.rcl.kduopass.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.presentation.viewmodel.AddAccountViewModel
import kduopass.composeapp.generated.resources.Res
import kduopass.composeapp.generated.resources.add_account_screen_save
import kduopass.composeapp.generated.resources.add_account_screen_secret
import kduopass.composeapp.generated.resources.add_account_screen_service_name
import kduopass.composeapp.generated.resources.add_account_screen_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    viewModel: AddAccountViewModel,
    navigateBack: () -> Unit
) {
    var state by remember { mutableStateOf(AddAccountState()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.add_account_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = state.serviceName,
                onValueChange = { state = state.copy(serviceName = it) },
                label = { Text(stringResource(Res.string.add_account_screen_service_name)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(16.dp))

            TextField(
                value = state.secret,
                onValueChange = { state = state.copy(secret = it) },
                label = { Text(stringResource(Res.string.add_account_screen_secret)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(24.dp))

            Button(
                onClick = {
                    val account = AccountEntity(
                        serviceName = state.serviceName,
                        secret = state.secret
                    )
                    viewModel.addAccount(account) {
                        navigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.add_account_screen_save))
            }
        }
    }
}

data class AddAccountState(
    val serviceName: String = "",
    val secret: String = ""
)