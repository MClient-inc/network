package ru.mclient.network.agreement.service

import org.springframework.stereotype.Service
import ru.mclient.network.agreement.domain.AgreementEntity
import ru.mclient.network.agreement.domain.AgreementEntity.Type.CLIENT_DATA_PROCESSING_AGREEMENT
import ru.mclient.network.agreement.domain.AgreementEntity.Type.USER_AGREEMENT
import ru.mclient.network.agreement.repository.UserAgreementRepository

@Service
class UserAgreementServiceImpl(
    private val repository: UserAgreementRepository,
) : UserAgreementService {


    private var cachedUserAgreement: AgreementEntity? = null
    private val userAgreementMonitor = Any()

    private var cachedClientAgreement: AgreementEntity? = null
    private val clientAgreementMonitor = Any()


    override fun updateUserAgreement(title: String, content: String): AgreementEntity {
        return synchronized(userAgreementMonitor) {
            repository.save(
                AgreementEntity(
                    title = title, content = content, type = USER_AGREEMENT,
                )
            ).also { cachedUserAgreement = it }
        }
    }

    override fun getUserAgreement(): AgreementEntity {
        return cachedUserAgreement ?: synchronized(userAgreementMonitor) {
            repository.findTopByTypeOrderByCreatedDateDesc(USER_AGREEMENT).also { cachedUserAgreement = it }
        }
    }

    override fun updateClientDataProcessingAgreement(title: String, content: String): AgreementEntity {
        return synchronized(clientAgreementMonitor) {
            repository.save(
                AgreementEntity(
                    title = title, content = content, type = CLIENT_DATA_PROCESSING_AGREEMENT,
                )
            ).also { cachedClientAgreement = it }
        }
    }

    override fun getClientDataProcessingAgreement(): AgreementEntity {
        return cachedClientAgreement ?: synchronized(clientAgreementMonitor) {
            repository.findTopByTypeOrderByCreatedDateDesc(CLIENT_DATA_PROCESSING_AGREEMENT)
                .also { cachedClientAgreement = it }
        }
    }


}