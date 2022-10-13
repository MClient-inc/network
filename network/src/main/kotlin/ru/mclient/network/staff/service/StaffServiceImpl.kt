package ru.mclient.network.staff.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.CompanyNetworkNotExists
import ru.mclient.network.staff.domain.StaffEntity
import ru.mclient.network.staff.repository.StaffRepository

@Service
class StaffServiceImpl(
    private val staffRepository: StaffRepository,
) : StaffService {
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
}