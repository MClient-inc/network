package ru.mclient.network.record.service

import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.clients.domain.ClientEntity
import ru.mclient.network.record.domain.RecordEntity
import ru.mclient.network.record.domain.ServiceToRecordEntity
import ru.mclient.network.record.repository.RecordsRepository
import ru.mclient.network.service.domain.ServiceToCompanyEntity
import ru.mclient.network.staff.domain.StaffEntity
import ru.mclient.network.staff.service.StaffService
import java.time.LocalDate
import java.time.LocalTime
import javax.transaction.Transactional

@Service
class RecordServiceImpl(
    private val recordsRepository: RecordsRepository,
    private val staffService: StaffService,
) : RecordService {


    override fun findRecordsByStaff(
        staff: List<StaffEntity>,
        start: LocalDate?,
        end: LocalDate?,
        limit: Int,
    ): List<RecordEntity> {
        return when {
            start == null && end == null -> {
                recordsRepository.findAllByScheduleStaffInAndScheduleDateAfter(
                    staff = staff, start = LocalDate.now(), pageable = Pageable.ofSize(limit)
                )
            }

            start == null && end != null -> {
                recordsRepository.findAllByScheduleStaffInAndScheduleDateBefore(
                    staff = staff, before = end, pageable = Pageable.ofSize(limit)
                )
            }

            start != null && end == null -> {
                recordsRepository.findAllByScheduleStaffInAndScheduleDateAfter(
                    staff = staff, start = start, pageable = Pageable.ofSize(limit)
                )
            }

            start != null && end != null -> {
                recordsRepository.findAllByScheduleStaffInAndScheduleDateBetween(
                    staff = staff, start = start, end = end, pageable = Pageable.ofSize(limit),
                )
            }

            else -> {
                throw IllegalStateException("Unreachable state (start = $start, end = $end)")
            }
        }
    }

    @Transactional
    override fun createRecord(
        staff: StaffEntity,
        date: LocalDate,
        time: LocalTime,
        client: ClientEntity,
        company: CompanyBranchEntity,
        services: List<ServiceToCompanyEntity>,
    ): RecordEntity {
        val schedule = staffService.findScheduleByStaff(staff, date) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "schedule not found"
        )
        val record = RecordEntity(
            schedule = schedule,
            client = client,
            time = time,
            company = company,
        )
        record.services = services.map { ServiceToRecordEntity(record = record, serviceToCompany = it) }
        return recordsRepository.save(record)
    }

}