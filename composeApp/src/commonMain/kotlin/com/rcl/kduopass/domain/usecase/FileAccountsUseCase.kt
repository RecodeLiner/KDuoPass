package com.rcl.kduopass.domain.usecase

import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.domain.repository.AccountRepository
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.sink
import kotlinx.io.buffered
import kotlinx.io.writeString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import me.tatarka.inject.annotations.Inject

class FileAccountsUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend fun importAccounts(file: PlatformFile, isClean: Boolean = false, batchSize: Int = 100) {
        if (!file.exists()) return
        if (isClean) repository.deleteAllAccounts()

        val raw = file.readString()
        val jsonArray = Json.parseToJsonElement(raw).jsonArray

        val buffer = mutableListOf<AccountEntity>()
        for (element in jsonArray) {
            buffer += Json.decodeFromJsonElement(AccountEntity.serializer(), element)
            if (buffer.size >= batchSize) {
                repository.insertAccountList(buffer.toList())
                buffer.clear()
            }
        }
        if (buffer.isNotEmpty()) {
            repository.insertAccountList(buffer)
        }
    }
    suspend fun exportAccounts(file: PlatformFile, batchSize: Int = 100) {
        val writer = file.sink(append = false).buffered()
        writer.writeString("[")
        var isFirst = true

        var offset = 0
        while (true) {
            val batch = repository.getAccountsBatch(batchSize, offset)
            if (batch.isEmpty()) break

            batch.forEach { account ->
                val json = Json.encodeToString(AccountEntity.serializer(), account)
                if (!isFirst) writer.writeString(",") else isFirst = false
                writer.writeString(json)
            }
            offset += batchSize
        }

        writer.writeString("]")
        writer.close()
    }
}