package ru.mclient.network.staff.service

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.staff.domain.StaffEntity

interface StaffService {

    fun findAllStaffForCompany(company: CompanyBranchEntity): List<StaffEntity>
    fun createStaff(name: String, role: String, company: CompanyBranchEntity): StaffEntity

}