package ru.mclient.network.record.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.record.domain.RecordEntity
import ru.mclient.network.record.domain.RecordPaymentEntity
import ru.mclient.network.record.domain.ServiceToRecordEntity
import ru.mclient.network.staff.domain.StaffEntity
import ru.mclient.network.staff.domain.StaffScheduleEntity
import java.time.LocalDate

interface RecordsRepository : CrudRepository<RecordEntity, Long> {

    fun findAllByScheduleIn(schedules: List<StaffScheduleEntity>): List<RecordEntity>

    fun findAllByScheduleStaffCompanyAndScheduleDate(company: CompanyBranchEntity, date: LocalDate): List<RecordEntity>


    fun findAllByScheduleStaffAndScheduleDateAfter(
        staff: StaffEntity,
        date: LocalDate,
        pageable: Pageable,
    ): List<RecordEntity>

    fun findAllByScheduleStaffInAndScheduleDateAfter(
        staff: List<StaffEntity>,
        start: LocalDate,
        pageable: Pageable,
    ): List<RecordEntity>

    fun findAllByScheduleStaffInAndScheduleDateBefore(
        staff: List<StaffEntity>,
        before: LocalDate,
        pageable: Pageable,
    ): List<RecordEntity>

    fun findAllByScheduleStaffInAndScheduleDate(
        staff: List<StaffEntity>,
        date: LocalDate,
        pageable: Pageable,
    ): List<RecordEntity>

    fun findAllByScheduleStaffAndScheduleDateBetween(
        staff: StaffEntity,
        start: LocalDate,
        end: LocalDate,
        pageable: Pageable,
    ): List<RecordEntity>

    fun findAllByScheduleStaffInAndScheduleDateBetween(
        staff: List<StaffEntity>,
        start: LocalDate,
        end: LocalDate,
        pageable: Pageable,
    ): List<RecordEntity>


    fun findAllByScheduleStaffCompanyAndScheduleDateBetween(
        company: CompanyBranchEntity,
        start: LocalDate,
        end: LocalDate,
        pageable: Pageable,
    ): List<RecordEntity>


    fun findAllByScheduleStaffCompanyAndScheduleDate(
        company: CompanyBranchEntity,
        date: LocalDate,
        pageable: Pageable,
    ): List<RecordEntity>


}

interface ServiceToRecordsRepository : CrudRepository<ServiceToRecordEntity, Long>

interface RecordPaymentRepository : CrudRepository<RecordPaymentEntity, Long> {

    fun deleteAllByRecord(record: RecordEntity)

}