package ru.mclient.network.network.repository

import org.springframework.data.repository.CrudRepository
import ru.mclient.network.account.domain.MClientAccountEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity

interface CompanyNetworkRepository: CrudRepository<CompanyNetworkEntity, Long> {

    fun findByCodenameIgnoreCase(codename: String): CompanyNetworkEntity?

    fun findFirstByOwnerAndDisableNull(owner: MClientAccountEntity): CompanyNetworkEntity?

    fun findAllByOwnerAndDisableNull(owner: MClientAccountEntity): List<CompanyNetworkEntity>

    fun existsByCodenameIgnoreCase(codename: String): Boolean

}