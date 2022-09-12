package ru.mclient.network.network.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.mclient.network.network.CompanyNetworkDisabled
import ru.mclient.network.network.CompanyNetworkAlreadyExists
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.network.repository.CompanyNetworkRepository
import javax.transaction.Transactional

@Service
class CompanyNetworkServiceImpl(
    private val companyNetworkRepository: CompanyNetworkRepository,
) : CompanyNetworkService {

    @Transactional
    override fun findCompanyNetworkById(id: Long, throwOnDisabled: Boolean): CompanyNetworkEntity? {
        val network = companyNetworkRepository.findByIdOrNull(id) ?: return null
        if (throwOnDisabled && network.disable != null)
            throw CompanyNetworkDisabled(network.id)
        return network
    }

    @Transactional
    override fun findCompanyNetworkByCodename(codename: String, throwOnDisabled: Boolean): CompanyNetworkEntity? {
        val network = companyNetworkRepository.findByCodename(codename) ?: return null
        if (throwOnDisabled && network.disable != null)
            throw CompanyNetworkDisabled(network.id)
        return network
    }

    @Transactional
    override fun createCompanyNetwork(codename: String, title: String): CompanyNetworkEntity {
        if (companyNetworkRepository.existsByCodename(codename))
            throw CompanyNetworkAlreadyExists(codename)
        return companyNetworkRepository.save(CompanyNetworkEntity(title = title, codename = codename))
    }

}