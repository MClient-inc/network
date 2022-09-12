package ru.mclient.network.network.service

import ru.mclient.network.network.domain.CompanyNetworkEntity

interface CompanyNetworkService {

    fun findCompanyNetworkById(id: Long, throwOnDisabled: Boolean = true): CompanyNetworkEntity?

    fun findCompanyNetworkByCodename(codename: String, throwOnDisabled: Boolean = true): CompanyNetworkEntity?

    fun createCompanyNetwork(codename: String, title: String): CompanyNetworkEntity

}