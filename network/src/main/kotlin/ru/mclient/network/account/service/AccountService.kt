package ru.mclient.network.account.service

import ru.mclient.network.account.domain.MClientAccountEntity

interface AccountService {


    fun findAccountFromCurrentContext(): MClientAccountEntity
}