package ru.mclient.network.service.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.service.service.ServiceCategoriesService
import ru.mclient.network.service.service.ServiceService
import ru.mclient.network.staff.service.StaffService
import javax.transaction.Transactional

@RestController
class ServiceController(
    val serviceCategoriesService: ServiceCategoriesService,
    val serviceService: ServiceService,
    val companyService: CompanyBranchService,
    val staffService: StaffService,
) {

    @Transactional
    @PutMapping("/services/{serviceId}/staff")
    fun pairServiceToStaff(@PathVariable serviceId: Long, @RequestBody data: PairServiceToStaffRequest) {
        val staff = staffService.findByStaffId(data.staffId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "staff not found"
        )
        val service = serviceService.findService(serviceId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "service not found"
        )
        val serviceToCompany =
            serviceService.findServiceToCompany(service, staff.company) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "service not found on staff's company"
            )
        serviceService.pairServiceToStaff(serviceToCompany, staff)
    }

    @Transactional
    @GetMapping("/services/{serviceId}")
    fun findServiceById(@PathVariable serviceId: Long): GetServiceResponse {
        val service = serviceService.findService(serviceId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "service not found"
        )
        return GetServiceResponse(
            service = GetServiceResponse.Service(
                service.id,
                service.title,
                service.cost,
            ),
            category = GetServiceResponse.ServiceCategory(
                id = service.category.id,
                title = service.category.title,
            )
        )
    }

    @Transactional
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

    @Transactional
    @GetMapping("/staff/{staffId}/services")
    fun getServicesForStaff(
        @PathVariable staffId: Long,
    ): GetServicesForStaffResponse {
        val staff = staffService.findByStaffId(staffId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "staff not found"
        )
        val services = serviceService.findServicesByStaff(staff)
        return GetServicesForStaffResponse(
            services = services.map { service ->
                GetServicesForStaffResponse.Service(
                    id = service.service.service.id,
                    categoryId = service.service.service.category.id,
                    title = service.service.service.title,
                    cost = service.service.cost,
                )
            }
        )
    }

    @Transactional
    @GetMapping("/services/{serviceId}/staff")
    fun getStaffForService(
        @PathVariable serviceId: Long,
        @RequestParam companyId: Long?,
    ): GetStaffForServiceResponse {
        val service = serviceService.findService(serviceId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "service not found"
        )
        val company = companyId?.let(companyService::findCompanyById)
        val servicesToStaff = serviceService.findServicesByService(service, company)
        return GetStaffForServiceResponse(
            staff = servicesToStaff.map { serviceToStaff ->
                GetStaffForServiceResponse.Staff(
                    id = serviceToStaff.staff.id,
                    name = serviceToStaff.staff.name,
                    codename = serviceToStaff.staff.codename,
                    role = serviceToStaff.staff.role,
                )
            }
        )
    }

    @Transactional
    @PostMapping("/categories/{categoryId}/services")
    fun createService(
        @PathVariable categoryId: Long,
        @RequestBody data: CreateServiceRequest,
    ): CreateServiceResponse {
        val company = data.companyId?.let(companyService::findCompanyById)
        val category = serviceCategoriesService.findByCategoryId(categoryId)
        val service = serviceService.createService(data.title, data.cost, category, company)
        return CreateServiceResponse(
            id = service.id,
            title = service.title,
            cost = service.cost,
            categoryId = category.id
        )
    }

}

class CreateServiceRequest(
    val title: String,
    val cost: Long,
    val companyId: Long?,
)

class CreateServiceResponse(
    val id: Long,
    val title: String,
    val cost: Long,
    val categoryId: Long,
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

class GetServicesForStaffResponse(
    val services: List<Service>,
) {

    class Service(
        val id: Long,
        val categoryId: Long,
        val title: String,
        val cost: Long,
    )

}

class GetStaffForServiceResponse(
    val staff: List<Staff>,
) {

    class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val role: String,
    )

}

class GetServiceResponse(
    val service: Service,
    val category: ServiceCategory,
) {
    data class Service(
        val id: Long,
        val title: String,
        val cost: Long,
    )

    data class ServiceCategory(
        val id: Long,
        val title: String,
    )
}

class PairServiceToStaffRequest(
    val staffId: Long,
)