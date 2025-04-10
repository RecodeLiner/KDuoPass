package com.rcl.kduopass.data.mapper

import com.rcl.kduopass.data.database.AccountEntity
import com.rcl.kduopass.domain.model.Account

fun AccountEntity.toDomainModel(): Account {
    return Account(
        id = this.id,
        serviceName = this.serviceName,
        secret = this.secret
    )
}

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        id = this.id,
        serviceName = this.serviceName,
        secret = this.secret
    )
}
