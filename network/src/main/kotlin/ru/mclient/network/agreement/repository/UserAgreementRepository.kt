package ru.mclient.network.agreement.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mclient.network.agreement.domain.AgreementEntity

interface UserAgreementRepository : JpaRepository<AgreementEntity, Long> {

    fun findTopByTypeOrderByCreatedDateDesc(type: AgreementEntity.Type): AgreementEntity

}