package ru.mclient.network.service.controller

import org.springframework.web.bind.annotation.*
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.service.service.ServiceCategoriesService
import ru.mclient.network.service.service.ServiceService

@RestController
class ServiceController(
    val serviceCategoriesService: ServiceCategoriesService,
    val serviceService: ServiceService,
    val companyService: CompanyBranchService,
) {

    @GetMapping("/categories/{categoryId}/services")
    fun getServicesForCategory(
        @PathVariable categoryId: Long,
        @RequestParam companyId: Long?,
    ): GetServicesForCategoryResponse {
        val company = companyId?.let(companyService::findCompanyById)
        if (company == null && companyId != null) {
            return GetServicesForCategoryResponse(
                services = emptyList()
            )
        }
        val category = serviceCategoriesService.findByCategoryId(categoryId)
        val services = serviceService.findServicesByCategory(category, company)
        return GetServicesForCategoryResponse(
            services = services.map { service ->
                GetServicesForCategoryResponse.Service(
                    id = service.id,
                    categoryId = service.category.id,
                    title = service.title,
                    cost = service.cost,
                )
            }
        )
    }

    @PostMapping("/categories/{categoryId}/services")
    fun createService(
        @PathVariable categoryId: Long,
        @RequestBody data: CreateServiceRequest,
    ): CreateServiceResponse {
        val company = data.companyId?.let(companyService::findCompanyById)
        val category = serviceCategoriesService.findByCategoryId(categoryId)
        val service = serviceService.createService(data.title, data.cost, category, company)
        return CreateServiceResponse(
            title = service.title,
            cost = service.cost,
        )
    }

}

class CreateServiceRequest(
    val title: String,
    val cost: Long,
    val companyId: Long?,
)

class CreateServiceResponse(
    val title: String,
    val cost: Long,
)

class GetServicesForCategoryResponse(
    val services: List<Service>,
) {

    class Service(
        val id: Long,
        val categoryId: Long,
        val title: String,
        val cost: Long,
    )

}