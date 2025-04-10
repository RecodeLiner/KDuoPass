package com.rcl.kduopass.domain.usecase

import com.rcl.kduopass.domain.model.Account
import com.rcl.kduopass.domain.repository.AccountRepository

class DeleteAccountUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(account: Account) = repository.deleteAccount(account)
}

