package com.rcl.kduopass.presentation.viewmodel.components

import com.rcl.kduopass.data.database.AccountEntity

data class AccountWithCode(
    val account: AccountEntity,
    val code: String
)