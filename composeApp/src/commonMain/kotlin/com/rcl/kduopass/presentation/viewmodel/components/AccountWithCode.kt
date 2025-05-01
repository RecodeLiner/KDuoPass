package com.rcl.kduopass.presentation.viewmodel.components

import com.rcl.kduopass.domain.model.Account

data class AccountWithCode(
    val account: Account,
    val code: String
)