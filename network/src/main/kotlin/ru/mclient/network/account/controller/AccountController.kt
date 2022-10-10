package ru.mclient.network.account.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mclient.network.account.service.AccountService


class GetAccountResponse(
    val id: Long,
    val username: String,
    val name: String,
)

@RestController
@RequestMapping("/account")
class AccountController(
    val accountService: AccountService,
) {


    @GetMapping
    fun get(): GetAccountResponse {
        val authentication = accountService.findAccountFromCurrentContext()
        return GetAccountResponse(
            id = authentication.id,
            username = authentication.username,
            name = authentication.name,
        )
    }

}