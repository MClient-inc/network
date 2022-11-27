package ru.mclient.network.record.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.abonement.domain.AbonementToClientEntity
import ru.mclient.network.abonement.service.AbonementService
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.clients.domain.ClientEntity
import ru.mclient.network.record.domain.RecordAbonementPaymentEntity
import ru.mclient.network.record.domain.RecordEntity
import ru.mclient.network.record.domain.ServiceToRecordEntity
import ru.mclient.network.record.repository.RecordAbonementPaymentRepository
import ru.mclient.network.record.repository.RecordPaymentRepository
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
    private val recordPaymentRepository: RecordPaymentRepository,
    private val recordAbonementPaymentRepository: RecordAbonementPaymentRepository,
    private val staffService: StaffService,
    private val abonementService: AbonementService,
) : RecordService {

    override fun findRecordById(recordId: Long): RecordEntity? {
        return recordsRepository.findByIdOrNull(recordId)
    }

    override fun findRecordsByStaff(
        staff: List<StaffEntity>,
        start: LocalDate?,
        end: LocalDate?,
        limit: Int,
    ): List<RecordEntity> {
        return when {
            start == null && end == null -> {
                recordsRepository.findAllByScheduleStaffInAndScheduleDateAfter(
                    staff = staff, start = LocalDate.now().minusDays(1), pageable = Pageable.ofSize(limit)
                )
            }

            start == null && end != null -> {
                recordsRepository.findAllByScheduleStaffInAndScheduleDateBefore(
                    staff = staff, before = end.plusDays(1), pageable = Pageable.ofSize(limit)
                )
            }

            start != null && end == null -> {
                recordsRepository.findAllByScheduleStaffInAndScheduleDateAfter(
                    staff = staff, start = start.minusDays(1), pageable = Pageable.ofSize(limit)
                )
            }

            start != null && end != null -> {
                recordsRepository.findAllByScheduleStaffInAndScheduleDateBetween(
                    staff = staff, start = start.minusDays(1), end = end.plusDays(1), pageable = Pageable.ofSize(limit),
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
            sum = services.sumOf { it.cost },
        )
        record.services = services.map { ServiceToRecordEntity(record = record, serviceToCompany = it) }
        return recordsRepository.save(record)
    }


    @Transactional
    override fun updateRecordVisitStatus(
        record: RecordEntity,
        newStatus: RecordEntity.VisitStatus,
    ) {
        if (record.status == newStatus)
            return
        record.status = newStatus
        recordPaymentRepository.deleteAllByRecord(record)
        recordsRepository.save(record)
    }

    @Transactional
    override fun payRecordWithAbonements(
        record: RecordEntity,
        abonements: List<AbonementToClientEntity>,
    ): List<RecordAbonementPaymentEntity> {
        if (record.status != RecordEntity.VisitStatus.COME)
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "record does not in code status"
            )
        val payments = abonements.map {
            RecordAbonementPaymentEntity(
                value = (it.cost / it.subabonement.usages),
                record = record,
                service = null,
                abonementToClient = it
            )
        }
        return recordAbonementPaymentRepository.saveAll(payments).toList()
    }

    override fun getRecordPayments(record: RecordEntity): List<RecordAbonementPaymentEntity> {
        return recordAbonementPaymentRepository.findAllByRecord(record)
    }

    override fun getRecordsInPeriod(
        company: CompanyBranchEntity,
        start: LocalDate,
        end: LocalDate,
    ): List<RecordEntity> {
        return recordsRepository.findAllByCompanyAndScheduleDateBetween(company, start, end)
    }
}