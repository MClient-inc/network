package ru.mclient.network.record.service

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.clients.domain.ClientEntity
import ru.mclient.network.record.domain.RecordEntity
import ru.mclient.network.record.domain.ServiceToRecordEntity
import ru.mclient.network.service.domain.ServiceToCompanyEntity
import ru.mclient.network.staff.domain.StaffEntity
import java.time.LocalDate
import java.time.LocalTime

interface RecordService {

    fun findRecordById(
        recordId: Long,
    ): RecordEntity?

    fun findRecordsByStaff(
        staff: List<StaffEntity>,
        start: LocalDate?,
        end: LocalDate?,
        limit: Int,
    ): List<RecordEntity>

    fun createRecord(staff: StaffEntity, date: LocalDate,  time: LocalTime, client: ClientEntity, company: CompanyBranchEntity, services: List<ServiceToCompanyEntity>): RecordEntity

}