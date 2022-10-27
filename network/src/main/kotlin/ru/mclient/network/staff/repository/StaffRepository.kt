package ru.mclient.network.staff.repository

import org.springframework.data.repository.CrudRepository
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.staff.domain.StaffEntity
import ru.mclient.network.staff.domain.StaffScheduleEntity
import java.time.LocalDate

interface StaffRepository : CrudRepository<StaffEntity, Long> {

    fun findAllByCompany(company: CompanyBranchEntity): List<StaffEntity>

    fun findByCodename(codename: String): StaffEntity?

}

interface StaffScheduleRepository : CrudRepository<StaffScheduleEntity, Long> {

    fun findByStaffAndDate(staff: StaffEntity, date: LocalDate): StaffScheduleEntity

    fun findByStaffInAndDate(staff: List<StaffEntity>, date: LocalDate): List<StaffScheduleEntity>

    fun findByStaffAndDateBetween(staff: StaffEntity, start: LocalDate, end: LocalDate): List<StaffScheduleEntity>

    fun findByStaffCompanyAndDateBetween(
        company: CompanyBranchEntity,
        start: LocalDate,
        end: LocalDate,
    ): List<StaffScheduleEntity>


    fun findByStaffCompanyAndDate(
        company: CompanyBranchEntity,
        date: LocalDate,
    ): List<StaffScheduleEntity>

}