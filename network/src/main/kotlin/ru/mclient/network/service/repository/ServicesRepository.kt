package ru.mclient.network.service.repository

import org.springframework.data.repository.CrudRepository
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.service.domain.ServiceCategoryEntity
import ru.mclient.network.service.domain.ServiceCategoryForCompanyEntity


interface ServiceCategoryRepository : CrudRepository<ServiceCategoryEntity, Long> {

    fun findAllByNetwork(network: CompanyNetworkEntity): List<ServiceCategoryEntity>

}

interface ServiceCategoryForCompanyRepository : CrudRepository<ServiceCategoryForCompanyEntity, Long> {

    fun findAllByCompany(company: CompanyBranchEntity): List<ServiceCategoryForCompanyEntity>


}