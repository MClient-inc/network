package ru.mclient.network.service.service

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.service.domain.ServiceCategoryEntity
import ru.mclient.network.service.domain.ServiceEntity

interface ServiceService {

    fun findServicesByCategory(category: ServiceCategoryEntity, company: CompanyBranchEntity?): List<ServiceEntity>
    fun createService(
        title: String,
        cost: Long,
        category: ServiceCategoryEntity,
        company: CompanyBranchEntity? = null
    ): ServiceEntity

}