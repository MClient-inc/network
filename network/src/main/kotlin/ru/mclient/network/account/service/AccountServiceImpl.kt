package ru.mclient.network.account.service

import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.account.domain.MClientAccountEntity
import ru.mclient.network.account.repository.MClientAccountRepository

@Service
class AccountServiceImpl(private val accounts: MClientAccountRepository) : AccountService {

    private fun findAccountByUsername(username: String): MClientAccountEntity {
        return accounts.findByUsername(username) ?: accounts.save(
            MClientAccountEntity(
                username = username,
                name = username
            )
        )
    }

    override fun findAccountFromCurrentContext(): MClientAccountEntity {
        val authorization = SecurityContextHolder.getContext().authentication
        if (authorization.isAuthenticated)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        return findAccountByUsername(authorization.name)
    }

}