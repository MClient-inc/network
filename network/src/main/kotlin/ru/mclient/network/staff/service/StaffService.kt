package ru.mclient.network.staff.service

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.staff.domain.StaffEntity

interface StaffService {

    fun findAllStaffForCompany(company: CompanyBranchEntity): List<StaffEntity>
    fun createStaff(name: String, codename: String, role: String, company: CompanyBranchEntity): StaffEntity
    fun findByIdOrCodename(query: String): StaffEntity

    fun findByCodename(codename: String): StaffEntity?
    fun findByStaffId(staffId: Long): StaffEntity?
}