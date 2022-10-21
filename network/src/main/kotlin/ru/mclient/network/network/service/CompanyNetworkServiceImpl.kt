package ru.mclient.network.network.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.mclient.network.account.domain.MClientAccountEntity
import ru.mclient.network.account.service.AccountService
import ru.mclient.network.network.CompanyNetworkAlreadyExists
import ru.mclient.network.network.CompanyNetworkDisabled
import ru.mclient.network.network.CompanyNetworkNotExists
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.network.repository.CompanyNetworkRepository
import javax.transaction.Transactional

@Service
class CompanyNetworkServiceImpl(
    private val companyNetworkRepository: CompanyNetworkRepository,
    private val accountService: AccountService,
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
        val network = companyNetworkRepository.findByCodenameIgnoreCase(codename) ?: return null
        if (throwOnDisabled && network.disable != null)
            throw CompanyNetworkDisabled(network.id)
        return network
    }

    override fun findAvailableCompanyNetworks(skipOnDisabled: Boolean): List<CompanyNetworkEntity> {
        return companyNetworkRepository.findAllByOwnerAndDisableNull(accountService.findAccountFromCurrentContext())
    }

    @Transactional
    override fun createCompanyNetwork(
        codename: String,
        title: String,
        owner: MClientAccountEntity,
    ): CompanyNetworkEntity {
        if (companyNetworkRepository.existsByCodenameIgnoreCase(codename))
            throw CompanyNetworkAlreadyExists(codename)
        return companyNetworkRepository.save(CompanyNetworkEntity(title = title, codename = codename, owner = owner))
    }

    override fun findFirstCompanyNetworkForAccount(
        account: MClientAccountEntity,
    ): CompanyNetworkEntity? {
        return companyNetworkRepository.findFirstByOwnerAndDisableNull(account)
    }

    override fun findByIdOrCodename(query: String): CompanyNetworkEntity {
        return when {
            query.firstOrNull()?.isDigit() == true -> {
                val id = query.toLong()
                findCompanyNetworkById(id)
                    ?: throw CompanyNetworkNotExists(query)
            }

            else -> findCompanyNetworkByCodename(query)
                ?: throw CompanyNetworkNotExists(query)

        }
    }
}