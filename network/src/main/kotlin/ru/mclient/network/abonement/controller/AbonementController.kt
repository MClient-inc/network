package ru.mclient.network.abonement.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.abonement.service.AbonementService
import ru.mclient.network.branch.service.CompanyBranchService
import java.time.LocalDateTime

@RestController
class AbonementController(
    val companyBranchService: CompanyBranchService,
    val abonementService: AbonementService,
) {

    @PostMapping("/companies/{companyQuery}/abonements")
    fun createAbonementForCompany(
        @PathVariable companyQuery: String,
        @RequestBody data: CreateAbonementsRequest,
    ): GetAbonementResponse {
        val company = companyBranchService.findByIdOrCodename(companyQuery)
        val abonement = abonementService.createAbonements(
            network = company.network,
            title = data.title,
            subabonements = data.subabonements.map {
                it.title to it.usages
            },
        )
        return GetAbonementResponse(
            id = abonement.id,
            title = abonement.title,
            subabonements = abonement.subabonements.map {
                GetAbonementResponse.Subabonement(
                    id = it.id,
                    title = it.title,
                    usages = it.usages,
                    liveTimeInMillis = it.liveTimeInMillis,
                    availableUntil = it.availableUntil,
                )
            }
        )
    }

    @GetMapping("/abonements/{abonementId}")
    fun getAbonement(
        @PathVariable abonementId: Long,
    ): GetAbonementResponse {
        val abonement = abonementService.findAbonementById(abonementId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "abonement not found"
        )
        return GetAbonementResponse(
            id = abonement.id,
            title = abonement.title,
            subabonements = abonement.subabonements.map {
                GetAbonementResponse.Subabonement(
                    id = it.id,
                    title = it.title,
                    usages = it.usages,
                    liveTimeInMillis = it.liveTimeInMillis,
                    availableUntil = it.availableUntil,
                )
            }
        )
    }

    @PutMapping("/abonements/{abonementId}/subabonements")
    fun addSubabonements(
        @PathVariable abonementId: Long,
        @RequestBody data: AddSubabonementsRequest,
    ): GetAbonementResponse {
        val abonement = abonementService.findAbonementById(abonementId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "abonement not found"
        )
        abonementService.addSubabonements(abonement, data.subabonements.map { it.title to it.usages })
        return GetAbonementResponse(
            id = abonement.id,
            title = abonement.title,
            subabonements = abonement.subabonements.map {
                GetAbonementResponse.Subabonement(
                    id = it.id,
                    title = it.title,
                    usages = it.usages,
                    liveTimeInMillis = it.liveTimeInMillis,
                    availableUntil = it.availableUntil,
                )
            }
        )
    }

    @GetMapping("/companies/{companyQuery}/abonements")
    fun getAbonementsForCompany(@PathVariable companyQuery: String): GetAbonementsResponse {
        val company = companyBranchService.findByIdOrCodename(companyQuery)
        val abonements = abonementService.findAbonementsForCompany(company)
        return GetAbonementsResponse(
            abonements = abonements.map { abonement ->
                GetAbonementsResponse.Abonement(
                    id = abonement.id,
                    title = abonement.title,
                    subabonements = abonement.subabonements.map { subabonement ->
                        GetAbonementsResponse.Subabonement(
                            subabonement.id,
                            subabonement.title,
                            subabonement.usages,
                            subabonement.liveTimeInMillis,
                            subabonement.availableUntil,
                        )
                    }
                )
            }
        )
    }

}

class AddSubabonementsRequest(
    val subabonements: List<CreateAbonementsRequest.Subabonement>,
) {
    class Subabonement(
        val title: String,
        val usages: Int,
    )

}

class CreateAbonementsRequest(
    val title: String,
    val subabonements: List<Subabonement>,
) {

    class Subabonement(
        val title: String,
        val usages: Int,
    )

}

class GetAbonementsResponse(
    val abonements: List<Abonement>,
) {

    class Abonement(
        val id: Long,
        val title: String,
        val subabonements: List<Subabonement>,
    )

    class Subabonement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        val availableUntil: LocalDateTime,
    )

}

class GetAbonementResponse(
    val id: Long,
    val title: String,
    val subabonements: List<Subabonement>,
) {

    class Subabonement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        val availableUntil: LocalDateTime,
    )
}