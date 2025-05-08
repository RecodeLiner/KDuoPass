package com.rcl.kduopass.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts")
    fun getAccounts(): Flow<List<AccountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    @Delete
    suspend fun deleteAccount(account: AccountEntity)
    @Query("DELETE FROM accounts")
    suspend fun deleteAllAccounts()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountList(accounts: List<AccountEntity>)
    @Query("SELECT * FROM accounts LIMIT :limit OFFSET :offset")
    suspend fun getAccountsBatch(limit: Int, offset: Int): List<AccountEntity>
}
