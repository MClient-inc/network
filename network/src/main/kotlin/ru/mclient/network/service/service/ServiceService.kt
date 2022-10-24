package ru.mclient.network.service.service

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.service.domain.ServiceCategoryEntity
import ru.mclient.network.service.domain.ServiceEntity
import ru.mclient.network.service.domain.ServiceToCompanyEntity
import ru.mclient.network.service.domain.ServiceToStaffEntity
import ru.mclient.network.staff.domain.StaffEntity

interface ServiceService {

    fun findService(serviceId: Long): ServiceEntity?

    fun findServiceToCompany(service: ServiceEntity, company: CompanyBranchEntity): ServiceToCompanyEntity?

    fun findServicesByCategory(category: ServiceCategoryEntity, company: CompanyBranchEntity?): List<ServiceEntity>

    fun createService(
        title: String,
        cost: Long,
        category: ServiceCategoryEntity,
        company: CompanyBranchEntity? = null,
    ): ServiceEntity

    fun findServicesByService(service: ServiceEntity, company: CompanyBranchEntity?): List<ServiceToStaffEntity>

    fun findServicesByStaff(staff: StaffEntity): List<ServiceToStaffEntity>

    fun pairServiceToStaff(serviceToStaffEntity: ServiceToCompanyEntity, staff: StaffEntity)

}