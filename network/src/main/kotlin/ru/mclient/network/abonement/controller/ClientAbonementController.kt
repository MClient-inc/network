package ru.mclient.network.abonement.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.abonement.domain.currentUsages
import ru.mclient.network.abonement.service.AbonementService
import ru.mclient.network.clients.service.ClientsService


@RestController
class ClientAbonementController(
    private val clientsService: ClientsService,
    private val abonementService: AbonementService,
) {

    @GetMapping("/clients/{clientId}/abonements")
    fun getAbonementsForClient(@PathVariable clientId: Long): GetClientAbonementsResponse {
        val client = clientsService.findClientById(clientId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "client not found"
        )
        val pairs = abonementService.findAbonementPairingsForClient(client)
        return GetClientAbonementsResponse(
            abonements = pairs.map { pair ->
                GetClientAbonementsResponse.ClientAbonement(
                    id = pair.id,
                    abonement = GetClientAbonementsResponse.Abonement(
                        id = pair.subabonement.abonement.id,
                        title = pair.subabonement.abonement.title,
                        subabonement = GetClientAbonementsResponse.Subabonement(
                            id = pair.subabonement.id,
                            title = pair.subabonement.title,
                            usages = pair.currentUsages,
                            maxUsages = pair.subabonement.usages,
                            cost = pair.subabonement.cost,
                        ),
                    ),
                    usages = pair.currentUsages,
                    cost = pair.cost,
                )
            }
        )
    }

    @PutMapping("/clients/{clientId}/abonements")
    fun addAbonementToClient(@PathVariable clientId: Long, @RequestBody body: AddAbonementToClientRequest) {
        val client = clientsService.findClientById(clientId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "client not found"
        )
        val subabonement = abonementService.findSubabonementById(body.subabonementId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "subabonement not found"
        )
        abonementService.addAbonementToClient(client, subabonement)
    }

}

class AddAbonementToClientRequest(
    val subabonementId: Long,
)


class GetClientAbonementsResponse(
    val abonements: List<ClientAbonement>,
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
        val usages: Int,
        val maxUsages: Int,
        val cost: Long,
    )

}