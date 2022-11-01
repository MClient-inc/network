package ru.mclient.network.staff.service

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.staff.domain.StaffEntity
import ru.mclient.network.staff.domain.StaffScheduleEntity
import java.time.LocalDate

interface StaffService {

    fun findAllStaffForCompany(company: CompanyBranchEntity): List<StaffEntity>

    fun createStaff(
        name: String,
        codename: String,
        role: String,
        company: CompanyBranchEntity,
    ): StaffEntity

    fun findByIdOrCodename(query: String): StaffEntity

    fun findByCodename(codename: String): StaffEntity?

    fun findByStaffId(staffId: Long): StaffEntity?

    fun findScheduleByStaff(
        staff: StaffEntity,
        start: LocalDate,
        end: LocalDate,
        limit: Int?,
    ): List<StaffScheduleEntity>

    fun findScheduleByStaff(
        staff: List<StaffEntity>,
        start: LocalDate,
        end: LocalDate,
        limit: Int,
    ): List<StaffScheduleEntity>

    fun findStaffByIds(
        staffId: List<Long>,
    ): List<StaffEntity>

    fun findScheduleByStaff(
        staff: StaffEntity,
        date: LocalDate,
    ): StaffScheduleEntity?

    fun findScheduleByCompany(
        company: CompanyBranchEntity,
        date: LocalDate,
        limit: Int?,
    ): Map<StaffEntity, List<StaffScheduleEntity>>

    fun findScheduleByStaff(
        staff: List<StaffEntity>,
        date: LocalDate,
        limit: Int?,
    ): Map<StaffEntity, List<StaffScheduleEntity>>

    fun addStaffSchedule(
        schedule: List<StaffScheduleEntity>,
    ): List<StaffScheduleEntity>

    fun findScheduleByStaffFrom(staff: StaffEntity, start: LocalDate, limit: Int): List<StaffScheduleEntity>

    fun findScheduleByStaffFrom(staff: List<StaffEntity>, start: LocalDate, limit: Int): List<StaffScheduleEntity>

    fun findScheduleByStaffTo(staff: List<StaffEntity>, end: LocalDate, limit: Int): List<StaffScheduleEntity>

}