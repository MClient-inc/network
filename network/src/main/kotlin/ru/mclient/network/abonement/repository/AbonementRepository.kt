package ru.mclient.network.abonement.repository

import org.springframework.data.repository.CrudRepository
import ru.mclient.network.abonement.domain.AbonementEntity
import ru.mclient.network.abonement.domain.SubabonementEntity
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity

interface AbonementRepository : CrudRepository<AbonementEntity, Long> {

    fun findAllByNetworkCompaniesContains(company: CompanyBranchEntity): List<AbonementEntity>

    fun findAllByNetwork(network: CompanyNetworkEntity): List<AbonementEntity>

}



interface SubabonementRepository : CrudRepository<SubabonementEntity, Long> {

}