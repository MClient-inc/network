package ru.mclient.network.staff.service

import org.springframework.stereotype.Service
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.staff.domain.StaffEntity
import ru.mclient.network.staff.repository.StaffRepository

@Service
class StaffServiceImpl(
    private val staffRepository: StaffRepository,
) : StaffService {
    override fun findAllStaffForCompany(company: CompanyBranchEntity): List<StaffEntity> {
        return staffRepository.findAllByCompany(company)
    }

    override fun createStaff(name: String, role: String, company: CompanyBranchEntity): StaffEntity {
        return staffRepository.save(
            StaffEntity(
                name = name,
                role = role,
                company = company,
            )
        )
    }
}