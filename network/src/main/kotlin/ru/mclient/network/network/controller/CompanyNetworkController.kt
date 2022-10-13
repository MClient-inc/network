package ru.mclient.network.network.controller

import org.springframework.web.bind.annotation.*
import ru.mclient.network.account.service.AccountService
import ru.mclient.network.network.CompanyNetworkNotExists
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.network.service.CompanyNetworkService


@RestController
class CompanyNetworkController(
    private val companyNetworkService: CompanyNetworkService,
    private val accounts: AccountService,
) {

    @GetMapping("/networks/{query}")
    fun getCompanyNetwork(@PathVariable query: String): GetCompanyNetworkResponse {
        val network = companyNetworkService.findByIdOrCodename(query)
        return GetCompanyNetworkResponse(
            id = network.id,
            title = network.title,
            codename = network.codename,
        )
    }

    @PostMapping("/networks")
    fun createCompanyNetwork(@RequestBody request: CreateCompanyNetworkRequest): CreateCompanyNetworkResponse {
        val chain = companyNetworkService.createCompanyNetwork(
            codename = request.title,
            title = request.codename,
            owner = accounts.findAccountFromCurrentContext(),
        )
        return CreateCompanyNetworkResponse(chain.id, chain.title, chain.codename)
    }

    @GetMapping("/networks")
    fun getAvailableCompanies(): GetAvailableCompanyNetworksResponse {
        val companyNetworks = companyNetworkService.findAvailableCompanyNetworks()
        return GetAvailableCompanyNetworksResponse(
            networks = companyNetworks.map { company ->
                GetAvailableCompanyNetworksResponse.CompanyNetwork(
                    id = company.id,
                    title = company.title,
                    codename = company.codename
                )
            }
        )
    }

}

class CreateCompanyNetworkRequest(
    val title: String,
    val codename: String,
)

class CreateCompanyNetworkResponse(
    val id: Long,
    val title: String,
    val codename: String,
)


class GetCompanyNetworkResponse(
    val id: Long,
    val title: String,
    val codename: String,
)

class GetAvailableCompanyNetworksResponse(
    val networks: List<CompanyNetwork>,
) {
    class CompanyNetwork(
        val id: Long,
        val title: String,
        val codename: String,
    )
}