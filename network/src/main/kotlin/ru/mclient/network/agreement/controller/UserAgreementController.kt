package ru.mclient.network.agreement.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.mclient.network.agreement.service.UserAgreementService

@RestController
class UserAgreementController(
    private val agreementService: UserAgreementService,
) {

    @GetMapping("/info/user-agreement")
    fun getUserAgreement(): AgreementResponse {
        val agreement = agreementService.getUserAgreement()
        return AgreementResponse(
            title = agreement.title,
            content = agreement.content,
        )
    }

    //TODO: authorize admin
    @PostMapping("/info/user-agreement")
    fun updateUserAgreement(@RequestBody body: UpdateAgreementRequest): AgreementResponse {
        val agreement = agreementService.updateUserAgreement(body.title, body.content)
        return AgreementResponse(
            title = agreement.title,
            content = agreement.content,
        )
    }

    @GetMapping("/info/data-processing-agreement")
    fun getClientAgreement(): AgreementResponse {
        val agreement = agreementService.getClientDataProcessingAgreement()
        return AgreementResponse(
            title = agreement.title,
            content = agreement.content,
        )
    }

    //TODO: authorize admin
    @PostMapping("/info/data-processing-agreement")
    fun updateClientAgreement(@RequestBody body: UpdateAgreementRequest): AgreementResponse {
        val agreement = agreementService.updateClientDataProcessingAgreement(body.title, body.content)
        return AgreementResponse(
            title = agreement.title,
            content = agreement.content,
        )
    }

}

class AgreementResponse(
    val title: String,
    val content: String,
)

class UpdateAgreementRequest(
    val title: String,
    val content: String,
)