package ru.mclient.network.staff.controllers

import org.springframework.web.bind.annotation.*
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.staff.service.StaffService

@RestController
class CompanyStaffController(
    private val companyService: CompanyBranchService,
    private val staffService: StaffService,
) {

    @GetMapping("/companies/{companyId}/staff")
    fun getStaffForCompany(@PathVariable companyId: String): GetStaffForCompanyResponse {
        val company = companyService.findByIdOrCodename(companyId)
        val staff = staffService.findAllStaffForCompany(company)
        return GetStaffForCompanyResponse(
            staff = staff.map {
                GetStaffForCompanyResponse.Staff(
                    id = it.id,
                    name = it.name,
                    role = it.role
                )
            }
        )
    }

    @PostMapping("/companies/{companyId}/staff")
    fun createStaff(@PathVariable companyId: String, @RequestBody request: CreateStaffRequest): CreateStaffResponse {
        val company = companyService.findByIdOrCodename(companyId)
        val staff = staffService.createStaff(request.name, request.role, company)
        return CreateStaffResponse(
            id = staff.id,
            name = staff.name,
            role = staff.role,
        )
    }

}


class CreateStaffRequest(
    val name: String,
    val role: String,
)

class CreateStaffResponse(
    val id: Long,
    val name: String,
    val role: String,
)

class GetStaffForCompanyResponse(
    val staff: List<Staff>,
) {
    class Staff(
        val id: Long,
        val name: String,
        val role: String,
    )
}