package ru.mclient.network.record.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.abonement.controller.GetClientAbonementsResponse
import ru.mclient.network.abonement.domain.currentUsages
import ru.mclient.network.abonement.service.AbonementService
import ru.mclient.network.record.domain.RecordEntity
import ru.mclient.network.record.service.RecordService
import javax.transaction.Transactional

@RestController
class RecordVisitController(
    private val recordService: RecordService,
    private val abonementService: AbonementService,
) {

    @PatchMapping("/records/{recordId}/status")
    fun editRecordStatus(
        @PathVariable recordId: Long,
        @RequestBody data: EditRecordStatusRequest,
    ) {
        val record = recordService.findRecordById(recordId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "record not found"
        )
        recordService.updateRecordVisitStatus(
            record, when (data.status) {
                EditRecordStatusRequest.RecordVisitStatus.WAITING -> RecordEntity.VisitStatus.WAITING
                EditRecordStatusRequest.RecordVisitStatus.COME -> RecordEntity.VisitStatus.COME
                EditRecordStatusRequest.RecordVisitStatus.NOT_COME -> RecordEntity.VisitStatus.NOT_COME
            }
        )
    }

    @Transactional
    @PatchMapping("/records/{recordId}/pay")
    fun editRecordPay(
        @PathVariable recordId: Long,
        @RequestBody data: EditRecordVisitRequest,
    ) {
        val record = recordService.findRecordById(recordId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "record not found"
        )
        val abonements = abonementService.findAbonementPairingsForClientAndIds(record.client, data.abonements)
            .flatMap { service -> List(data.abonements.count { it == service.id }) { service } }
        if (abonements.size != data.abonements.size) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "some abonements not found")
        }
        recordService.payRecordWithAbonements(
            record = record,
            abonements = abonements,
        )
    }

    @Transactional
    @GetMapping("/records/{recordId}/pay")
    fun getRecordPay(
        @PathVariable recordId: Long,
    ): GetRecordPaymentResponse {
        val record = recordService.findRecordById(recordId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "record not found"
        )
        val payments = recordService.getRecordPayments(record = record,)
        return GetRecordPaymentResponse(
            abonements = payments.map {
                GetRecordPaymentResponse.ClientAbonement(
                    id = it.abonementToClient.id,
                    abonement = GetRecordPaymentResponse.Abonement(
                        id = it.abonementToClient.subabonement.abonement.id,
                        title = it.abonementToClient.subabonement.abonement.title,
                        subabonement = GetRecordPaymentResponse.Subabonement(
                            id = it.abonementToClient.subabonement.id,
                            title = it.abonementToClient.subabonement.title,
                            maxUsages = it.abonementToClient.subabonement.usages,
                            cost = it.abonementToClient.subabonement.cost,
                        )
                    ),
                    usages = it.abonementToClient.currentUsages,
                    cost = it.abonementToClient.cost,
                )
            }
        )
    }

}

class GetRecordPaymentResponse(
    val abonements: List<ClientAbonement>
) {
    class ClientAbonement(
        val id: Long,
        val abonement: Abonement,
        val usages: Int,
        val cost: Long,
    )

    class Abonement(
        val id: Long,
        val title: String,
        val subabonement: Subabonement,
    )


    class Subabonement(
        val id: Long,
        val title: String,
        val maxUsages: Int,
        val cost: Long,
    )

}

class EditRecordStatusRequest(
    val status: RecordVisitStatus,
) {
    enum class RecordVisitStatus {
        WAITING, COME, NOT_COME
    }
}

class EditRecordVisitRequest(
    val abonements: List<Long>,
)