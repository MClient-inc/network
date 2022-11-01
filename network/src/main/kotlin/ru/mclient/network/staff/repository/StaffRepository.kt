package ru.mclient.network.staff.repository

import org.springframework.data.domain.Pageable
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

    fun findByStaffAndDate(staff: StaffEntity, date: LocalDate): StaffScheduleEntity?

    fun findAllByStaffAndDateAfter(staff: StaffEntity, date: LocalDate, pageable: Pageable): List<StaffScheduleEntity>

    fun findAllByStaffInAndDateAfter(
        staff: List<StaffEntity>,
        date: LocalDate,
        pageable: Pageable,
    ): List<StaffScheduleEntity>

    fun findAllByStaffInAndDateBefore(
        staff: List<StaffEntity>,
        date: LocalDate,
        pageable: Pageable,
    ): List<StaffScheduleEntity>

    fun findAllByStaffInAndDate(staff: List<StaffEntity>, date: LocalDate, pageable: Pageable): List<StaffScheduleEntity>

    fun findAllByStaffAndDateBetween(
        staff: StaffEntity,
        start: LocalDate,
        end: LocalDate,
        pageable: Pageable,
    ): List<StaffScheduleEntity>

    fun findAllByStaffInAndDateBetween(
        staff: List<StaffEntity>,
        start: LocalDate,
        end: LocalDate,
        pageable: Pageable,
    ): List<StaffScheduleEntity>


    fun findAllByStaffCompanyAndDateBetween(
        company: CompanyBranchEntity,
        start: LocalDate,
        end: LocalDate,
        pageable: Pageable,
    ): List<StaffScheduleEntity>


    fun findAllByStaffCompanyAndDate(
        company: CompanyBranchEntity,
        date: LocalDate,
        pageable: Pageable,
    ): List<StaffScheduleEntity>

}