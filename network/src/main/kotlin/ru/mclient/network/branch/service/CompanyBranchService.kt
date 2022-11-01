package ru.mclient.network.branch.service

import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.CompanyNetworkNotExists

interface CompanyBranchService {

    fun findCompanyById(id: Long, throwOnDisabled: Boolean = true): CompanyBranchEntity?

    fun findCompanyByCodename(codename: String, throwOnDisabled: Boolean = true): CompanyBranchEntity?

    fun findCompanyBranchesForNetwork(network: CompanyNetworkEntity): List<CompanyBranchEntity>

    fun createCompanyBranch(codename: String, title: String, network: CompanyNetworkEntity): CompanyBranchEntity

    fun findByIdOrCodename(query: String): CompanyBranchEntity
}