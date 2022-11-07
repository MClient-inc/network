package ru.mclient.network.abonement.service

import ru.mclient.network.abonement.domain.AbonementEntity
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.service.domain.ServiceEntity

interface AbonementService {

    fun findAbonementsForCompany(company: CompanyBranchEntity): List<AbonementEntity>

    fun findAbonementById(abonementId: Long): AbonementEntity?

    fun createAbonements(
        network: CompanyNetworkEntity,
        title: String,
        subabonements: List<Pair<String, Int>>,
    ): AbonementEntity

    fun addSubabonements(abonement: AbonementEntity, subabonements: List<Pair<String, Int>>): AbonementEntity

    fun addServices(abonement: AbonementEntity, services: List<ServiceEntity>): AbonementEntity


}
