package ru.mclient.network.abonement.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.mclient.network.abonement.domain.AbonementEntity
import ru.mclient.network.abonement.domain.ServiceToAbonementEntity
import ru.mclient.network.abonement.domain.SubabonementEntity
import ru.mclient.network.abonement.repository.AbonementRepository
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import ru.mclient.network.service.domain.ServiceEntity
import java.time.LocalDateTime

@Service
class AbonementServiceImpl(
    private val abonementRepository: AbonementRepository,
) : AbonementService {

    override fun findAbonementsForCompany(company: CompanyBranchEntity): List<AbonementEntity> {
        return abonementRepository.findAllByNetwork(company.network)
    }

    override fun addSubabonements(
        abonement: AbonementEntity,
        subabonements: List<Pair<String, Int>>,
    ): AbonementEntity {
        abonement.subabonements = abonement.subabonements + subabonements.map {
            SubabonementEntity(
                title = it.first,
                usages = it.second,
                availableUntil = LocalDateTime.now().plusDays(365),
                liveTimeInMillis = 31_536_000_000,
                abonement = abonement,
            )
        }
        return abonementRepository.save(abonement)
    }

    override fun addServices(abonement: AbonementEntity, services: List<ServiceEntity>): AbonementEntity {
        val serviceToAbonements = services.map {
            ServiceToAbonementEntity(
                service = it,
                abonement = abonement
            )
        }
        abonement.services = abonement.services + serviceToAbonements
        abonementRepository.save(abonement)
        return abonement
    }

    override fun createAbonements(
        network: CompanyNetworkEntity,
        title: String,
        subabonements: List<Pair<String, Int>>,
        services: List<ServiceEntity>,
    ): AbonementEntity {
        val abonement = abonementRepository.save(
            AbonementEntity(
                title = title,
                network = network,
            )
        )
        abonement.services = services.map {
            ServiceToAbonementEntity(
                service = it,
                abonement = abonement,
            )
        }
        abonement.subabonements = subabonements.map {
            SubabonementEntity(
                title = it.first,
                usages = it.second,
                availableUntil = LocalDateTime.now().plusDays(365),
                liveTimeInMillis = 31_536_000_000,
                abonement = abonement,
            )
        }
        return abonementRepository.save(abonement)
    }


    override fun findAbonementById(abonementId: Long): AbonementEntity? {
        return abonementRepository.findByIdOrNull(abonementId)
    }
}