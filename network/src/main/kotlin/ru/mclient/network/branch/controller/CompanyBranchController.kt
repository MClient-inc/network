package ru.mclient.network.branch.controller

import org.springframework.web.bind.annotation.*
import ru.mclient.network.account.service.AccountService
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.network.CompanyNetworkNotExists
import ru.mclient.network.network.service.CompanyNetworkService


@RestController
class CompanyBranchController(
    val companyBranchService: CompanyBranchService,
    private val accountService: AccountService,
    private val companyNetworkService: CompanyNetworkService,
) {


    @GetMapping("/branches/{query}")
    fun getCompanyBranch(@PathVariable query: String): GetCompanyBranchResponse {
        val company = findByIdOrCodename(query)
        return GetCompanyBranchResponse(
            id = company.id, title = company.title, codename = company.codename, networkId = company.network.id
        )
    }

    @GetMapping("/networks/{networkId}/companies")
    fun getBranchesForNetwork(@PathVariable networkId: Long): GetCompanyBranchesForNetworkResponse {
        val network = companyNetworkService.findCompanyNetworkById(networkId)
            ?: throw CompanyNetworkNotExists(networkId.toString())
        val companies = companyBranchService.findCompanyBranchesForNetwork(network)
        return GetCompanyBranchesForNetworkResponse(
            networkId = networkId,
            companies = companies.map {
                GetCompanyBranchesForNetworkResponse.CompanyBranch(
                    id = it.id,
                    title = it.title,
                    codename = it.codename
                )
            }
        )
    }

    @PostMapping("/companies")
    fun createCompany(@RequestBody request: CreateCompanyRequest): CreateCompanyResponse {
        val account = accountService.findAccountFromCurrentContext()
        val network = if (request.networkId == null) {
            companyNetworkService.findFirstCompanyNetworkForAccount(account = account)
        } else {
            companyNetworkService.findCompanyNetworkById(request.networkId, throwOnDisabled = false)
        } ?: companyNetworkService.createCompanyNetwork(
            codename = "${request.codename}_network",
            title = "Сеть ${request.title}",
            owner = account
        )
        val company = companyBranchService.createCompanyBranch(request.codename, request.title, network)
        return CreateCompanyResponse(
            id = company.id,
            codename = company.codename,
            title = company.title,
            networkId = network.id,
            description = "",
        )
    }

    private fun findByIdOrCodename(query: String): CompanyBranchEntity {
        return when {
            query.firstOrNull()?.isDigit() == true -> {
                val id = query.toLong()
                companyBranchService.findCompanyById(id)
                    ?: throw CompanyNetworkNotExists(query)
            }

            else -> companyBranchService.findCompanyByCodename(query)
                ?: throw CompanyNetworkNotExists(query)

        }
    }

}


class CreateCompanyRequest(
    val networkId: Long? = null,
    val codename: String,
    val title: String,
    val description: String,
)

class CreateCompanyResponse(
    val id: Long,
    val networkId: Long,
    val codename: String,
    val title: String,
    val description: String,
)


class GetCompanyBranchResponse(
    val id: Long,
    val title: String,
    val codename: String,
    val networkId: Long,
)


class GetCompanyBranchesForNetworkResponse(
    val networkId: Long,
    val companies: List<CompanyBranch>,
) {
    class CompanyBranch(
        val id: Long,
        val title: String,
        val codename: String,
    )
}