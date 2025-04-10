package com.rcl.kduopass.data.repository

import com.rcl.kduopass.data.database.AccountDao
import com.rcl.kduopass.data.mapper.toDomainModel
import com.rcl.kduopass.data.mapper.toEntity
import com.rcl.kduopass.domain.model.Account
import com.rcl.kduopass.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

class AccountRepositoryImpl @Inject constructor(
    private val dao: AccountDao
) : AccountRepository {
    override fun getAccounts(): Flow<List<Account>> =
        dao.getAccounts().map { it.map { entity -> entity.toDomainModel() } }

    override suspend fun addAccount(account: Account) =
        dao.insertAccount(account.toEntity())

    override suspend fun deleteAccount(account: Account) =
        dao.deleteAccount(account.toEntity())
}