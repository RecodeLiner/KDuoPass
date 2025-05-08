package com.rcl.kduopass.data.repository

import com.rcl.kduopass.data.database.AccountDao
import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

class AccountRepositoryImpl @Inject constructor(
    private val dao: AccountDao
) : AccountRepository {
    override fun getAccounts(): Flow<List<AccountEntity>> =
        dao.getAccounts()

    override suspend fun addAccount(account: AccountEntity) =
        dao.insertAccount(account)

    override suspend fun deleteAccount(account: AccountEntity) =
        dao.deleteAccount(account)

    override suspend fun deleteAllAccounts() {
        dao.deleteAllAccounts()
    }

    override suspend fun insertAccountList(accounts: List<AccountEntity>) {
        dao.insertAccountList(accounts)
    }

    override suspend fun getAccountsBatch(limit: Int, offset: Int): List<AccountEntity> =
        dao.getAccountsBatch(limit, offset)
}