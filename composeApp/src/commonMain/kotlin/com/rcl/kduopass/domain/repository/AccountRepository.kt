package com.rcl.kduopass.domain.repository

import com.rcl.kduopass.data.database.AccountEntity
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccounts(): Flow<List<AccountEntity>>
    suspend fun addAccount(account: AccountEntity)
    suspend fun deleteAccount(account: AccountEntity)
    suspend fun deleteAllAccounts()
    suspend fun insertAccountList(accounts: List<AccountEntity>)
    suspend fun getAccountsBatch(limit: Int, offset: Int): List<AccountEntity>
}
