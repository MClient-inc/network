package ru.mclient.network.service.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.network.service.CompanyNetworkService
import ru.mclient.network.service.service.ServiceCategoriesService

@RestController
class ServiceCategoriesController(
    private val serviceCategoriesService: ServiceCategoriesService,
    private val companyNetworkService: CompanyNetworkService,
    private val companyBranchService: CompanyBranchService,
) {


    @GetMapping("/companies/{companyId}/categories")
    fun getServiceCategoriesForCompany(@PathVariable companyId: Long): GetServiceCategoriesForCompanyResponse {
        val company =
            companyBranchService.findCompanyById(companyId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val services = serviceCategoriesService.findServiceCategoriesByCompany(company)
        return GetServiceCategoriesForCompanyResponse(
            categories = services.map { service ->
                GetServiceCategoriesForCompanyResponse.ServiceCategory(
                    id = service.category.id,
                    title = service.category.title,
                )
            }
        )
    }

    @GetMapping("/networks/{networkId}/categories")
    fun getServiceCategoriesForNetwork(@PathVariable networkId: Long): GetServiceCategoriesForNetworkResponse {
        val network =
            companyNetworkService.findCompanyNetworkById(networkId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val services = serviceCategoriesService.findServiceCategoriesByNetwork(network)
        return GetServiceCategoriesForNetworkResponse(
            services = services.map { service ->
                GetServiceCategoriesForNetworkResponse.ServiceCategory(
                    id = service.id,
                    title = service.title,
                )
            }
        )
    }

    @PostMapping("/companies/{companyId}/categories")
    fun createServiceCategoryForCompany(
        @PathVariable companyId: Long,
        @RequestBody data: CreateServiceCategoryRequest,
    ): CreateServiceCategoryResponse {
        val company = companyBranchService.findCompanyById(companyId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val category = serviceCategoriesService.createServiceCategoriesForCompany(data.title, company)
        return CreateServiceCategoryResponse(
            id = category.category.id,
            title = category.category.title,
        )
    }

    @PostMapping("/networks/{networkId}/categories")
    fun createServiceCategoryForNetwork(
        @PathVariable networkId: Long,
        @RequestBody data: CreateServiceCategoryRequest,
    ): CreateServiceCategoryResponse {
        val network = companyNetworkService.findCompanyNetworkById(networkId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val category = serviceCategoriesService.createServiceCategoriesForNetwork(data.title, network)
        return CreateServiceCategoryResponse(
            id = category.id,
            title = category.title,
        )
    }

    @GetMapping("/categories/{categoryId}")
    fun getServiceCategoryById(
        @PathVariable categoryId: Long,
    ): GetServiceCategoryResponse {
        val service = serviceCategoriesService.findByCategoryId(categoryId)
        return GetServiceCategoryResponse(service.id, service.title)
    }

}

class GetServiceCategoryResponse(
    val id: Long,
    val title: String,
)

class GetServiceCategoriesForNetworkResponse(
    val services: List<ServiceCategory>,
) {
    data class ServiceCategory(
        val id: Long,
        val title: String,
    )
}

class GetServiceCategoriesForCompanyResponse(
    val categories: List<ServiceCategory>,
) {
    data class ServiceCategory(
        val id: Long,
        val title: String,
    )
}

class CreateServiceCategoryRequest(
    val title: String,
)

data class CreateServiceCategoryResponse(
    val id: Long,
    val title: String,
)