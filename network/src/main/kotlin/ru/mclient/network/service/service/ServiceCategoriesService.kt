package ru.mclient.network.service.service

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.service.domain.ServiceCategoryEntity
import ru.mclient.network.service.domain.ServiceCategoryForCompanyEntity

interface ServiceCategoriesService {

    fun findServiceCategoriesByCompany(company: CompanyBranchEntity): List<ServiceCategoryForCompanyEntity>

    fun findServiceCategoriesByNetwork(network: CompanyNetworkEntity): List<ServiceCategoryEntity>

    fun createServiceCategoriesForCompany(title: String,company: CompanyBranchEntity): ServiceCategoryForCompanyEntity


    fun createServiceCategoriesForNetwork(title: String,network: CompanyNetworkEntity): ServiceCategoryEntity
}