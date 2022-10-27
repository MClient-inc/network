package ru.mclient.network.staff.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.CompanyNetworkNotExists
import ru.mclient.network.staff.domain.StaffEntity
import ru.mclient.network.staff.domain.StaffScheduleEntity
import ru.mclient.network.staff.repository.StaffRepository
import ru.mclient.network.staff.repository.StaffScheduleRepository
import java.time.LocalDate

@Service
class StaffServiceImpl(
    private val staffRepository: StaffRepository,
    private val staffScheduleRepository: StaffScheduleRepository,
) : StaffService {

    override fun findScheduleByStaff(
        staff: List<StaffEntity>,
        date: LocalDate,
    ): Map<StaffEntity, List<StaffScheduleEntity>> {
        return staffScheduleRepository.findByStaffInAndDate(staff, date).groupBy { it.staff }
    }

    override fun findAllStaffForCompany(company: CompanyBranchEntity): List<StaffEntity> {
        return staffRepository.findAllByCompany(company)
    }

    override fun createStaff(name: String, codename: String, role: String, company: CompanyBranchEntity): StaffEntity {
        return staffRepository.save(
            StaffEntity(
                name = name,
                codename = codename,
                role = role,
                company = company,
            )
        )
    }

    override fun findByStaffId(staffId: Long): StaffEntity? {
        return staffRepository.findByIdOrNull(staffId)
    }

    override fun findByCodename(codename: String): StaffEntity? {
        return staffRepository.findByCodename(codename)
    }

    override fun findByIdOrCodename(query: String): StaffEntity {
        return when {
            query.firstOrNull()?.isDigit() == true -> {
                val id = query.toLong()
                findByStaffId(id)
                    ?: throw CompanyNetworkNotExists(query)
            }

            else -> findByCodename(query)
                ?: throw CompanyNetworkNotExists(query)

        }
    }

    override fun findScheduleByStaff(staff: StaffEntity, start: LocalDate, end: LocalDate): List<StaffScheduleEntity> {
        return staffScheduleRepository.findByStaffAndDateBetween(staff, start, end)
    }

    override fun findScheduleByStaff(staff: StaffEntity, date: LocalDate): StaffScheduleEntity? {
        return staffScheduleRepository.findByStaffAndDate(staff, date)
    }

    override fun findScheduleByCompany(
        company: CompanyBranchEntity,
        date: LocalDate,
    ): Map<StaffEntity, List<StaffScheduleEntity>> {
        return staffScheduleRepository.findByStaffCompanyAndDate(company, date).groupBy { it.staff }
    }

    override fun addStaffSchedule(schedule: List<StaffScheduleEntity>): List<StaffScheduleEntity> {
        return staffScheduleRepository.saveAll(schedule).toList()
    }

    override fun findStaffByIds(staffId: List<Long>): List<StaffEntity> {
        return staffRepository.findAllById(staffId).toList()
    }
}