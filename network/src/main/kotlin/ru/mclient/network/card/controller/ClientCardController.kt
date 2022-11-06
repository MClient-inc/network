package ru.mclient.network.card.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.clients.service.ClientsService

@RestController
class ClientCardController(
    @Value("\${feature.cards.url}")
    private val url: String,
    private val clientsService: ClientsService,
) {

    @GetMapping("/clients/{clientId}/card")
    fun getClientCard(@PathVariable clientId: Long): GetCardResponse {
        val client = clientsService.findClientById(clientId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "client not found"
        )
        return GetCardResponse(
            cardUrl = url + client.id + "?source=qrcode"
        )
    }

}


class GetCardResponse(
    val cardUrl: String,
)