package ru.mclient.network.branch.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.branch.repository.CompanyRepository
import ru.mclient.network.network.CompanyNetworkAlreadyExists
import ru.mclient.network.network.CompanyNetworkDisabled
import ru.mclient.network.network.CompanyNetworkNotExists
import ru.mclient.network.network.domain.CompanyNetworkEntity
import javax.transaction.Transactional

@Service
class CompanyBranchServiceImpl(
    private val companyRepository: CompanyRepository,
) : CompanyBranchService {


    @Transactional
    override fun findCompanyByCodename(codename: String, throwOnDisabled: Boolean): CompanyBranchEntity? {
        val branch = companyRepository.findByCodename(codename) ?: return null
        if (throwOnDisabled && branch.disable != null)
            throw CompanyNetworkDisabled(branch.id)
        return branch
    }

    @Transactional
    override fun createCompanyBranch(
        codename: String,
        title: String,
        network: CompanyNetworkEntity,
    ): CompanyBranchEntity {
        if (network.disable != null)
            throw CompanyNetworkDisabled(network.id.toString())
        if (companyRepository.existsByCodename(codename))
            throw CompanyNetworkAlreadyExists(network.id)
        return companyRepository.save(CompanyBranchEntity(title = title, codename = codename, network = network))
    }

    override fun findCompanyBranchesForNetwork(network: CompanyNetworkEntity): List<CompanyBranchEntity> {
        return companyRepository.findAllByNetwork(network)
    }

    @Transactional
    override fun findCompanyById(id: Long, throwOnDisabled: Boolean): CompanyBranchEntity? {
        val branch = companyRepository.findByIdOrNull(id) ?: return null
        if (throwOnDisabled && branch.disable != null)
            throw CompanyNetworkDisabled(branch.network.id)
        return branch
    }

    override fun findByIdOrCodename(query: String): CompanyBranchEntity {
        return when {
            query.firstOrNull()?.isDigit() == true -> {
                val id = query.toLong()
                findCompanyById(id)
                    ?: throw CompanyNetworkNotExists(query)
            }

            else -> findCompanyByCodename(query)
                ?: throw CompanyNetworkNotExists(query)

        }
    }

}