package com.rcl.kduopass.domain.usecase

data class AccountUseCases(
    val getAccounts: GetAccountsUseCase,
    val addAccount: AddAccountUseCase,
    val deleteAccount: DeleteAccountUseCase,
    val generateCode: GenerateCodeUseCase
)