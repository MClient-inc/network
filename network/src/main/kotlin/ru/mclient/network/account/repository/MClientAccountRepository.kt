package ru.mclient.network.account.repository

import org.springframework.data.repository.CrudRepository
import ru.mclient.network.account.domain.MClientAccountEntity

interface MClientAccountRepository: CrudRepository<MClientAccountEntity, Long> {

    fun findByUsername(username: String): MClientAccountEntity?

}