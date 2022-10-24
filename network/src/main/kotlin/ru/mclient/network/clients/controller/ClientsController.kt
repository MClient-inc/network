package ru.mclient.network.clients.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.clients.service.ClientsService
import ru.mclient.network.network.service.CompanyNetworkService

@RestController
class ClientsController(
    private val companyBranchService: CompanyBranchService,
    private val companyNetworkService: CompanyNetworkService,
    private val clientsService: ClientsService,
) {

    @GetMapping("/companies/{companyId}/clients")
    fun getClientByCompany(@PathVariable companyId: Long): GetClientsByCompany {
        val company = companyBranchService.findCompanyById(companyId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "company not found"
        )
        val clients = clientsService.findClientsForCompany(company)
        return GetClientsByCompany(
            clients = clients.map {
                GetClientsByCompany.Client(
                    id = it.id,
                    name = it.name,
                    phone = it.phone,
                )
            }
        )
    }

    @GetMapping("/networks/{networkId}/clients")
    fun getClientByNetwork(@PathVariable networkId: Long): GetClientsByNetwork {
        val network = companyNetworkService.findCompanyNetworkById(networkId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "network not found"
        )
        val clients = clientsService.findClientsForNetwork(network)
        return GetClientsByNetwork(
            clients = clients.map {
                GetClientsByNetwork.Client(
                    id = it.id,
                    name = it.name,
                    phone = it.phone,
                )
            }
        )
    }

    @PostMapping("/companies/{companyId}/clients")
    fun createClientsForCompany(
        @PathVariable companyId: Long,
        @RequestBody data: CreateClientRequest,
    ): CreateClientResponse {
        val company = companyBranchService.findCompanyById(companyId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "company not found",
        )
        val client = clientsService.createClient(data.name, data.phone, company)
        return CreateClientResponse(
            id = client.id,
            name = client.name,
            phone = client.phone,
            networkId = client.network.id,
        )
    }
    @PostMapping("/networks/{networkId}/clients")
    fun createClientsForNetwork(
        @PathVariable networkId: Long,
        @RequestBody data: CreateClientRequest,
    ): CreateClientResponse {
        val network = companyNetworkService.findCompanyNetworkById(networkId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "network not found",
        )
        val client = clientsService.createClient(data.name, data.phone, network)
        return CreateClientResponse(
            id = client.id,
            name = client.name,
            phone = client.phone,
            networkId = client.network.id,
        )
    }

}


class GetClientsByCompany(
    val clients: List<Client>,
) {
    class Client(
        val id: Long,
        val name: String,
        val phone: String?,
    )
}

class GetClientsByNetwork(
    val clients: List<Client>,
) {
    class Client(
        val id: Long,
        val name: String,
        val phone: String?,
    )
}

class CreateClientRequest(
    val name: String,
    val phone: String?,
)

class CreateClientResponse(
    val id: Long,
    val name: String,
    val phone: String?,
    val networkId: Long,
)