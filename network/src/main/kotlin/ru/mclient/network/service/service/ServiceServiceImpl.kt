package ru.mclient.network.service.service

import org.springframework.stereotype.Service
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.service.domain.ServiceCategoryEntity
import ru.mclient.network.service.domain.ServiceEntity
import ru.mclient.network.service.domain.ServiceToCompanyEntity
import ru.mclient.network.service.repository.ServiceToCompanyRepository
import ru.mclient.network.service.repository.ServicesRepository
import javax.transaction.Transactional

@Service
class ServiceServiceImpl(
    private val serviceToCompanyRepository: ServiceToCompanyRepository,
    private val servicesRepository: ServicesRepository,
    private val categoryService: ServiceCategoriesService,
) : ServiceService {
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
        category: ServiceCategoryEntity,
        company: CompanyBranchEntity?,
    ): ServiceEntity {
        val service = servicesRepository.save(ServiceEntity(title = title, category = category))
        if (company != null) {
            val categoryToCompany = categoryService.findByCategoryAndCompany(category = category, company = company)
            serviceToCompanyRepository.save(
                ServiceToCompanyEntity(
                    service = service,
                    categoryToCompany = categoryToCompany
                )
            )
        }
        return service
    }
}