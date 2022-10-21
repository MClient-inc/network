package ru.mclient.network.branch.repository

import org.springframework.data.repository.CrudRepository
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity


interface CompanyRepository : CrudRepository<CompanyBranchEntity, Long> {

    fun findByCodenameIgnoreCase(codename: String): CompanyBranchEntity?

    fun existsByCodenameIgnoreCase(codename: String): Boolean

    fun findAllByNetworkAndDisableNull(network: CompanyNetworkEntity): List<CompanyBranchEntity>

    fun findAllByNetwork(network: CompanyNetworkEntity): List<CompanyBranchEntity> {
        return findAllByNetworkAndDisableNull(network)
    }

}