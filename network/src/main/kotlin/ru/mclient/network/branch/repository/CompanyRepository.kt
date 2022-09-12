package ru.mclient.network.branch.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.CrudRepository
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity


interface CompanyRepository : CrudRepository<CompanyBranchEntity, Long> {

    fun findByCodename(codename: String): CompanyBranchEntity?

    fun existsByCodename(codename: String): Boolean

    fun findAllByNetworkAndDisableNull(network: CompanyNetworkEntity): List<CompanyBranchEntity>

    fun findAllByNetwork(network: CompanyNetworkEntity): List<CompanyBranchEntity> {
        return findAllByNetworkAndDisableNull(network)
    }

}