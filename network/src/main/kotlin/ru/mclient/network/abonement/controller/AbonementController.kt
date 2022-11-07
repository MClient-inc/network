package ru.mclient.network.abonement.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.abonement.domain.AbonementEntity
import ru.mclient.network.abonement.service.AbonementService
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.service.service.ServiceService
import java.time.LocalDateTime

@RestController
class AbonementController(
    val companyBranchService: CompanyBranchService,
    val abonementService: AbonementService,
    val serviceService: ServiceService,
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
        return abonement.toResponse()
    }

    @GetMapping("/abonements/{abonementId}")
    fun getAbonement(
        @PathVariable abonementId: Long,
    ): GetAbonementResponse {
        val abonement = abonementService.findAbonementById(abonementId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "abonement not found"
        )
        return abonement.toResponse()
    }

    private fun AbonementEntity.toResponse(): GetAbonementResponse {
        return GetAbonementResponse(
            id = id,
            title = title,
            subabonements = subabonements.map {
                GetAbonementResponse.Subabonement(
                    id = it.id,
                    title = it.title,
                    usages = it.usages,
                    liveTimeInMillis = it.liveTimeInMillis,
                    availableUntil = it.availableUntil,
                )
            },
            services = services.map {
                GetAbonementResponse.PairedService(
                    id = it.id,
                    title = it.service.title,
                    cost = it.service.cost
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
        return abonement.toResponse()
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

    @PutMapping("/abonements/{abonementId}/services")
    fun addServices(
        @PathVariable abonementId: Long,
        @RequestBody data: AddServicesRequest,
    ): GetAbonementResponse {
        val abonement = abonementService.findAbonementById(abonementId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "abonement not found"
        )
        val services = serviceService.findServicesByIds(data.services)
        abonementService.addServices(abonement, services)
        return abonement.toResponse()
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
class AddServicesRequest(
    val services: List<Long>,
)

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
    val services: List<PairedService>,
) {

    class Subabonement(
        val id: Long,
        val title: String,
        val usages: Int,
        val liveTimeInMillis: Long,
        val availableUntil: LocalDateTime,
    )

    class PairedService(
        val id: Long,
        val title: String,
        val cost: Long,
    )

}