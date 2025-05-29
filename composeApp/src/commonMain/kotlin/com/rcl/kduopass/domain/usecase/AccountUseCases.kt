package com.rcl.kduopass.domain.usecase

import me.tatarka.inject.annotations.Inject

data class AccountUseCases @Inject constructor(
    val getAccounts: GetAccountsUseCase,
    val addAccount: AddAccountUseCase,
    val deleteAccount: DeleteAccountUseCase,
    val generateCode: GenerateCodeUseCase
)