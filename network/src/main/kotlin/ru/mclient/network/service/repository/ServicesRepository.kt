package ru.mclient.network.service.repository

import org.springframework.data.repository.CrudRepository
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.service.domain.ServiceCategoryEntity
import ru.mclient.network.service.domain.ServiceCategoryToCompanyEntity
import ru.mclient.network.service.domain.ServiceEntity
import ru.mclient.network.service.domain.ServiceToCompanyEntity


interface ServiceCategoryRepository : CrudRepository<ServiceCategoryEntity, Long> {

    fun findAllByNetwork(network: CompanyNetworkEntity): List<ServiceCategoryEntity>

}

interface ServiceCategoryForCompanyRepository : CrudRepository<ServiceCategoryToCompanyEntity, Long> {

    fun findAllByCompany(company: CompanyBranchEntity): List<ServiceCategoryToCompanyEntity>

    fun findByCategoryAndCompany(category: ServiceCategoryEntity, company: CompanyBranchEntity): ServiceCategoryToCompanyEntity

}

interface ServicesRepository: CrudRepository<ServiceEntity, Long> {

    fun findAllByCategory(category: ServiceCategoryEntity): List<ServiceEntity>

}

interface ServiceToCompanyRepository: CrudRepository<ServiceToCompanyEntity, Long> {

    fun findAllByCategoryToCompany(categoryToCompany: ServiceCategoryToCompanyEntity): List<ServiceToCompanyEntity>

}