package ru.mclient.network.clients.service

import org.springframework.stereotype.Service
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.clients.domain.ClientEntity
import ru.mclient.network.clients.domain.ClientToCompanyEntity
import ru.mclient.network.clients.repository.ClientsRepository
import ru.mclient.network.clients.repository.ClientsToCompanyRepository
import ru.mclient.network.network.domain.CompanyNetworkEntity
import javax.transaction.Transactional

@Service
class ClientsServiceImpl(
    private val clientsRepository: ClientsRepository,
    private val clientsToCompanyRepository: ClientsToCompanyRepository,
) : ClientsService {

    override fun findClientsForCompany(company: CompanyBranchEntity): List<ClientEntity> {
        return clientsToCompanyRepository.findAllByCompany(company).map { it.client }
    }

    override fun findClientsForNetwork(network: CompanyNetworkEntity): List<ClientEntity> {
        return clientsRepository.findAllByNetwork(network)
    }

    @Transactional
    override fun createClient(name: String, phone: String?, company: CompanyBranchEntity): ClientEntity {
        val client = createClient(name, phone, company.network)
        clientsToCompanyRepository.save(ClientToCompanyEntity(client, company))
        return client
    }

    override fun createClient(name: String, phone: String?, network: CompanyNetworkEntity): ClientEntity {
        return clientsRepository.save(ClientEntity(phone, name, network))
    }

}