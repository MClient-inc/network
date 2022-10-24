package ru.mclient.network.service.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.service.domain.ServiceCategoryEntity
import ru.mclient.network.service.domain.ServiceEntity
import ru.mclient.network.service.domain.ServiceToCompanyEntity
import ru.mclient.network.service.domain.ServiceToStaffEntity
import ru.mclient.network.service.repository.ServiceToCompanyRepository
import ru.mclient.network.service.repository.ServiceToStaffRepository
import ru.mclient.network.service.repository.ServicesRepository
import ru.mclient.network.staff.domain.StaffEntity
import javax.transaction.Transactional

@Service
class ServiceServiceImpl(
    private val serviceToCompanyRepository: ServiceToCompanyRepository,
    private val servicesRepository: ServicesRepository,
    private val categoryService: ServiceCategoriesService,
    private val serviceToStaffRepository: ServiceToStaffRepository,
) : ServiceService {

    override fun findServiceToCompany(service: ServiceEntity, company: CompanyBranchEntity): ServiceToCompanyEntity? {
        return serviceToCompanyRepository.findByServiceAndCategoryToCompanyCompany(service, company)
    }

    override fun pairServiceToStaff(serviceToStaffEntity: ServiceToCompanyEntity, staff: StaffEntity) {
        serviceToStaffRepository.save(ServiceToStaffEntity(serviceToStaffEntity, staff))
    }

    override fun findServicesByStaff(staff: StaffEntity): List<ServiceToStaffEntity> {
        return serviceToStaffRepository.findByStaff(staff)
    }

    override fun findServicesByService(
        service: ServiceEntity,
        company: CompanyBranchEntity?,
    ): List<ServiceToStaffEntity> {
        return if (company != null)
            serviceToStaffRepository.findByServiceServiceAndStaffCompany(service, company)
        else
            serviceToStaffRepository.findByServiceService(service)
    }

    override fun findService(serviceId: Long): ServiceEntity? {
        return servicesRepository.findByIdOrNull(serviceId)
    }

    override fun findServicesByCategory(
        category: ServiceCategoryEntity,
        company: CompanyBranchEntity?,
    ): List<ServiceEntity> {
        return if (company == null) {
            servicesRepository.findAllByCategory(category)
        } else {
            val categoryForCompany = categoryService.findByCategoryAndCompany(category, company)
            return serviceToCompanyRepository.findAllByCategoryToCompany(categoryForCompany).map { it.service }
        }
    }

    @Transactional
    override fun createService(
        title: String,
        cost: Long,
        category: ServiceCategoryEntity,
        company: CompanyBranchEntity?,
    ): ServiceEntity {
        val service = servicesRepository.save(ServiceEntity(title = title, cost = cost, category = category))
        if (company != null) {
            val categoryToCompany = categoryService.findByCategoryAndCompany(category = category, company = company)
            serviceToCompanyRepository.save(
                ServiceToCompanyEntity(
                    service = service,
                    categoryToCompany = categoryToCompany,
                    cost = cost,
                )
            )
        }
        return service
    }

}