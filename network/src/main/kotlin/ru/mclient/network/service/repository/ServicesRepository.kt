package ru.mclient.network.service.repository

import org.springframework.data.repository.CrudRepository
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.service.domain.*
import ru.mclient.network.staff.domain.StaffEntity


interface ServiceCategoryRepository : CrudRepository<ServiceCategoryEntity, Long> {

    fun findAllByNetwork(network: CompanyNetworkEntity): List<ServiceCategoryEntity>

}

interface ServiceCategoryForCompanyRepository : CrudRepository<ServiceCategoryToCompanyEntity, Long> {

    fun findAllByCompany(company: CompanyBranchEntity): List<ServiceCategoryToCompanyEntity>

    fun findByCategoryAndCompany(
        category: ServiceCategoryEntity,
        company: CompanyBranchEntity,
    ): ServiceCategoryToCompanyEntity

}

interface ServicesRepository : CrudRepository<ServiceEntity, Long> {

    fun findAllByCategory(category: ServiceCategoryEntity): List<ServiceEntity>

}

interface ServiceToCompanyRepository : CrudRepository<ServiceToCompanyEntity, Long> {

    fun findAllByCategoryToCompany(categoryToCompany: ServiceCategoryToCompanyEntity): List<ServiceToCompanyEntity>

    fun findByServiceAndCategoryToCompanyCompany(
        service: ServiceEntity,
        company: CompanyBranchEntity,
    ): ServiceToCompanyEntity?

    fun findAllByServiceIdInAndCategoryToCompanyCompany(serviceIds: List<Long>, company: CompanyBranchEntity): List<ServiceToCompanyEntity>

}

interface ServiceToStaffRepository : CrudRepository<ServiceToStaffEntity, Long> {

    fun findByStaff(staff: StaffEntity): List<ServiceToStaffEntity>

    fun findByServiceService(service: ServiceEntity): List<ServiceToStaffEntity>

    fun findByServiceServiceAndStaffCompany(
        service: ServiceEntity,
        company: CompanyBranchEntity,
    ): List<ServiceToStaffEntity>


}