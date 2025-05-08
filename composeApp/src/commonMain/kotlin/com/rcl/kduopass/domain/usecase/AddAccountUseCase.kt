package com.rcl.kduopass.domain.usecase

import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.domain.repository.AccountRepository
import me.tatarka.inject.annotations.Inject

class AddAccountUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(account: AccountEntity) = repository.addAccount(account)
}