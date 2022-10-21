package ru.mclient.network.service.service

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.service.domain.ServiceCategoryEntity
import ru.mclient.network.service.domain.ServiceCategoryToCompanyEntity

interface ServiceCategoriesService {

    fun findServiceCategoriesByCompany(company: CompanyBranchEntity): List<ServiceCategoryToCompanyEntity>

    fun findServiceCategoriesByNetwork(network: CompanyNetworkEntity): List<ServiceCategoryEntity>

    fun createServiceCategoriesForCompany(title: String, company: CompanyBranchEntity): ServiceCategoryToCompanyEntity

    fun createServiceCategoriesForNetwork(title: String, network: CompanyNetworkEntity): ServiceCategoryEntity

    fun findByCategoryAndCompany(
        category: ServiceCategoryEntity,
        company: CompanyBranchEntity,
    ): ServiceCategoryToCompanyEntity

    fun findByCategoryId(
        categoryId: Long,
    ): ServiceCategoryEntity

}