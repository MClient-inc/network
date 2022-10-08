package ru.mclient.network.account.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mclient.network.account.service.AccountService


class GetAccountResponse(
    val username: String,
)

@RestController
@RequestMapping("/account")
class AccountController {


    @GetMapping
    fun get(authentication: Authentication): GetAccountResponse {
        return GetAccountResponse(
            authentication.name
        )
    }

}