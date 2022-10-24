package ru.mclient.network.clients.repository

import org.springframework.data.repository.CrudRepository
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.clients.domain.ClientEntity
import ru.mclient.network.clients.domain.ClientToCompanyEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity

interface ClientsRepository : CrudRepository<ClientEntity, Long> {

    fun findAllByNetwork(network: CompanyNetworkEntity): List<ClientEntity>

}

interface ClientsToCompanyRepository : CrudRepository<ClientToCompanyEntity, Long> {

    fun findAllByCompany(company: CompanyBranchEntity): List<ClientToCompanyEntity>

}