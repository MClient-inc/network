package ru.mclient.network.abonement.service

import ru.mclient.network.abonement.domain.AbonementEntity
import ru.mclient.network.abonement.domain.AbonementToClientEntity
import ru.mclient.network.abonement.domain.SubabonementEntity
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.clients.domain.ClientEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.service.domain.ServiceEntity

interface AbonementService {

    fun findAbonementsForCompany(company: CompanyBranchEntity): List<AbonementEntity>

    fun findAbonementPairingsForClient(client: ClientEntity): List<AbonementToClientEntity>

    fun findAbonementPairingsForClientAndIds(client: ClientEntity, pairs: List<Long>): List<AbonementToClientEntity>

    fun addAbonementToClient(client: ClientEntity, subabonement: SubabonementEntity): AbonementToClientEntity

    fun findAbonementById(abonementId: Long): AbonementEntity?

    fun findSubabonementById(abonementId: Long): SubabonementEntity?

    fun createAbonements(
        network: CompanyNetworkEntity,
        title: String,
        subabonements: List<Triple<String, Int, Long>>,
        services: List<ServiceEntity>,
    ): AbonementEntity

    fun addSubabonements(
        abonement: AbonementEntity,
        subabonements: List<Triple<String, Int, Long>>,
    ): AbonementEntity

    fun addServices(abonement: AbonementEntity, services: List<ServiceEntity>): AbonementEntity


}
