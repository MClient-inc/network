package ru.mclient.network.clients.service

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.clients.domain.ClientEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity

interface ClientsService {

    fun findClientsForCompany(company: CompanyBranchEntity): List<ClientEntity>

    fun findClientsForNetwork(network: CompanyNetworkEntity): List<ClientEntity>

    fun createClient(name: String, phone: String?, company: CompanyBranchEntity): ClientEntity

    fun createClient(name: String, phone: String?, network: CompanyNetworkEntity): ClientEntity

}