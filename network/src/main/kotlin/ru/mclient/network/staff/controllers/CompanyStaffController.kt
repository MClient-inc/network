package ru.mclient.network.staff.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.staff.service.StaffService

@RestController
class CompanyStaffController(
    private val companyService: CompanyBranchService,
    private val staffService: StaffService,
) {

    @Tag(name = "Staff")
    @GetMapping("/companies/{companyId}/staff")
    fun getStaffForCompany(@PathVariable companyId: String): GetStaffForCompanyResponse {
        val company = companyService.findByIdOrCodename(companyId)
        val staff = staffService.findAllStaffForCompany(company)
        return GetStaffForCompanyResponse(
            staff = staff.map {
                GetStaffForCompanyResponse.Staff(
                    id = it.id,
                    name = it.name,
                    codename = it.codename,
                    role = it.role
                )
            }
        )
    }

    @Tag(name = "Staff")
    @PostMapping("/companies/{companyId}/staff")
    fun createStaff(@PathVariable companyId: String, @RequestBody request: CreateStaffRequest): CreateStaffResponse {
        val company = companyService.findByIdOrCodename(companyId)
        val staff = staffService.createStaff(
            name = request.name,
            codename = request.codename,
            role = request.role,
            company = company
        )
        return CreateStaffResponse(
            id = staff.id,
            name = staff.name,
            codename = staff.codename,
            role = staff.role,
        )
    }

    @Tag(name = "Staff")
    @GetMapping("/staff/{staffId}")
    fun getStaffById(@PathVariable staffId: String): GetStaffResponse {
        val staff = staffService.findByIdOrCodename(staffId)
        return GetStaffResponse(
            id = staff.id,
            name = staff.name,
            codename = staff.codename,
            role = staff.role,
        )
    }
}


class CreateStaffRequest(
    val name: String,
    val codename: String,
    val role: String,
)

class CreateStaffResponse(
    val id: Long,
    val name: String,
    val codename: String,
    val role: String,
)

class GetStaffForCompanyResponse(
    val staff: List<Staff>,
) {
    class Staff(
        val id: Long,
        val name: String,
        val codename: String,
        val role: String,
    )
}

class GetStaffResponse(
    val id: Long,
    val name: String,
    val codename: String,
    val role: String,
)
