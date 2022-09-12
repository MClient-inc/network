package ru.mclient.network.network.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.network.CompanyNetworkNotExists
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.network.service.CompanyNetworkService


@RestController
@RequestMapping("/networks")
class CompanyNetworkController(
    private val companyNetworkService: CompanyNetworkService,
) {

    @GetMapping("/getCompanyNetwork")
    fun getCompanyNetwork(@RequestBody request: GetCompanyNetworkRequest): GetCompanyNetworkResponse {
        val network = findByIdOrCodename(request.id, request.codename)
        return GetCompanyNetworkResponse(
            id = network.id,
            title = network.title,
            codename = network.codename,
        )
    }

    @PostMapping("/createCompanyNetwork")
    fun createCompanyNetwork(@RequestBody request: CreateCompanyNetworkRequest): CreateCompanyNetworkResponse {
        val chain = companyNetworkService.createCompanyNetwork(request.title, request.codename)
        return CreateCompanyNetworkResponse(chain.id, chain.title, chain.codename)
    }


    private fun findByIdOrCodename(id: Long?, codename: String?): CompanyNetworkEntity {
        return when {
            id != null && codename != null -> throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "You should set id OR codename. Not together!"
            )

            id != null -> companyNetworkService.findCompanyNetworkById(id)
                ?: throw CompanyNetworkNotExists(id.toString())
            codename != null -> companyNetworkService.findCompanyNetworkByCodename(codename)
                ?: throw CompanyNetworkNotExists(codename)

            else -> throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "You should set id OR codename. Not together!"
            )
        }
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

class GetCompanyNetworkRequest(
    val id: Long?,
    val codename: String?,
)