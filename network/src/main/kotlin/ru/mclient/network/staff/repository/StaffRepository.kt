package ru.mclient.network.staff.repository

import org.springframework.data.repository.CrudRepository
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.staff.domain.StaffEntity

interface StaffRepository : CrudRepository<StaffEntity, Long> {

    fun findAllByCompany(company: CompanyBranchEntity): List<StaffEntity>

}