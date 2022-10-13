package ru.mclient.network.network.service

import ru.mclient.network.account.domain.MClientAccountEntity
import ru.mclient.network.network.CompanyNetworkNotExists
import ru.mclient.network.network.domain.CompanyNetworkEntity

interface CompanyNetworkService {

    fun findCompanyNetworkById(id: Long, throwOnDisabled: Boolean = true): CompanyNetworkEntity?


    fun findFirstCompanyNetworkForAccount(
        account: MClientAccountEntity,
    ): CompanyNetworkEntity?

    fun findCompanyNetworkByCodename(codename: String, throwOnDisabled: Boolean = true): CompanyNetworkEntity?

    fun findAvailableCompanyNetworks(skipOnDisabled: Boolean = true): List<CompanyNetworkEntity>

    fun createCompanyNetwork(codename: String, title: String, owner: MClientAccountEntity): CompanyNetworkEntity

    fun findByIdOrCodename(query: String): CompanyNetworkEntity

}