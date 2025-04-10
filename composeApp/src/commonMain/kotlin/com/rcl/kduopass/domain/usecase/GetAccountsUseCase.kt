package com.rcl.kduopass.domain.usecase

import com.rcl.kduopass.domain.model.Account
import com.rcl.kduopass.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetAccountsUseCase(private val repository: AccountRepository) {
    operator fun invoke(): Flow<List<Account>> = repository.getAccounts()
}