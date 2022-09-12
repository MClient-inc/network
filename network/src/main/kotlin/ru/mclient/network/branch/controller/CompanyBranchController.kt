package ru.mclient.network.branch.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.network.CompanyNetworkNotExists
import ru.mclient.network.network.service.CompanyNetworkService
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.branch.service.CompanyBranchService


@RestController
@RequestMapping("/branches")
class CompanyBranchController(
    val companyBranchService: CompanyBranchService,
    val companyNetworkService: CompanyNetworkService,
) {


    @GetMapping("/getCompanyBranch")
    fun getCompanyBranch(@RequestBody request: GetCompanyBranchRequest): GetCompanyBranchResponse {
        val company = findByIdOrCodename(request.id, request.codename)
        return GetCompanyBranchResponse(
            id = company.id, title = company.title, codename = company.codename, networkId = company.network.id
        )
    }

    @GetMapping("/getCompanyBranchesForNetwork")
    fun getBranchesForNetwork(@RequestBody request: GetCompanyBranchesForNetworkRequest): GetCompanyBranchesForNetworkResponse {
        val network = companyNetworkService.findCompanyNetworkById(request.networkId)
            ?: throw CompanyNetworkNotExists(request.networkId.toString())
        val companies = companyBranchService.findCompanyBranchesForNetwork(network)
        return GetCompanyBranchesForNetworkResponse(
            networkId = request.networkId,
            branches = companies.map {
                GetCompanyBranchesForNetworkResponse.CompanyBranch(
                    id = it.id,
                    title = it.title,
                    codename = it.codename
                )
            }
        )
    }

    @PostMapping("createCompanyBranch")
    fun createCompany(@RequestBody request: CreateCompanyRequest): CreateCompanyResponse {
        val network =
            companyNetworkService.findCompanyNetworkById(request.networkId, throwOnDisabled = false)
                ?: throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Chain does not exists"
                )
        val company = companyBranchService.createCompanyBranch(request.codename, request.title, network)
        return CreateCompanyResponse(
            id = company.id,
            codename = company.codename,
            title = company.title,
            networkId = request.networkId,
        )
    }

    private fun findByIdOrCodename(id: Long?, codename: String?): CompanyBranchEntity {
        return when {
            id != null && codename != null ->
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "You should set id OR codename. Not together!")

            id != null -> companyBranchService.findCompanyById(id)
                ?: throw CompanyNetworkNotExists(id.toString())

            codename != null -> companyBranchService.findCompanyByCodename(codename)
                ?: throw CompanyNetworkNotExists(codename)

            else ->
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "You should set id OR codename. Not together!")
        }
    }

}


class CreateCompanyRequest(
    val networkId: Long,
    val codename: String,
    val title: String,
)

class CreateCompanyResponse(
    val id: Long,
    val networkId: Long,
    val codename: String,
    val title: String,
)

class GetCompanyBranchRequest(
    val codename: String?,
    val id: Long?,
)

class GetCompanyBranchResponse(
    val id: Long,
    val title: String,
    val codename: String,
    val networkId: Long,
)

class GetCompanyBranchesForNetworkRequest(
    val networkId: Long,
)

class GetCompanyBranchesForNetworkResponse(
    val networkId: Long,
    val branches: List<CompanyBranch>,
) {
    class CompanyBranch(
        val id: Long,
        val title: String,
        val codename: String,
    )
}