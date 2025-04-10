package com.rcl.kduopass.domain.repository

import com.rcl.kduopass.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccounts(): Flow<List<Account>>
    suspend fun addAccount(account: Account)
    suspend fun deleteAccount(account: Account)
}
