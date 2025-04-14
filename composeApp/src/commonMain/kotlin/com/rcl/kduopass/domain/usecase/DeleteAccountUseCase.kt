package com.rcl.kduopass.domain.usecase

import com.rcl.kduopass.domain.model.Account
import com.rcl.kduopass.domain.repository.AccountRepository
import me.tatarka.inject.annotations.Inject

class DeleteAccountUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(account: Account) = repository.deleteAccount(account)
}

