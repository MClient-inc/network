package ru.mclient.network.service.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.service.domain.ServiceCategoryEntity
import ru.mclient.network.service.domain.ServiceCategoryToCompanyEntity
import ru.mclient.network.service.repository.ServiceCategoryForCompanyRepository
import ru.mclient.network.service.repository.ServiceCategoryRepository
import javax.transaction.Transactional


@Service
class ServiceCategoriesServiceImpl(
    private val serviceCategoryForCompanyRepository: ServiceCategoryForCompanyRepository,
    private val serviceCategoryRepository: ServiceCategoryRepository,
) : ServiceCategoriesService {

    override fun findServiceCategoriesByCompany(company: CompanyBranchEntity): List<ServiceCategoryToCompanyEntity> {
        return serviceCategoryForCompanyRepository.findAllByCompany(company)
    }

    override fun findServiceCategoriesByNetwork(network: CompanyNetworkEntity): List<ServiceCategoryEntity> {
        return serviceCategoryRepository.findAllByNetwork(network)
    }

    override fun createServiceCategoriesForNetwork(
        title: String,
        network: CompanyNetworkEntity,
    ): ServiceCategoryEntity {
        return serviceCategoryRepository.save(ServiceCategoryEntity(title = title, network = network))
    }

    @Transactional
    override fun createServiceCategoriesForCompany(
        title: String,
        company: CompanyBranchEntity,
    ): ServiceCategoryToCompanyEntity {
        val category = createServiceCategoriesForNetwork(title, company.network)
        return serviceCategoryForCompanyRepository.save(
            ServiceCategoryToCompanyEntity(
                company = company,
                category = category
            )
        )
    }

    override fun findByCategoryAndCompany(
        category: ServiceCategoryEntity,
        company: CompanyBranchEntity,
    ): ServiceCategoryToCompanyEntity {
        return serviceCategoryForCompanyRepository.findByCategoryAndCompany(category, company)
    }

    override fun findByCategoryId(categoryId: Long): ServiceCategoryEntity {
        return serviceCategoryRepository.findByIdOrNull(categoryId) ?: TODO()
    }
}