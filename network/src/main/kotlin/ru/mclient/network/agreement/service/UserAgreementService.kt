package ru.mclient.network.agreement.service

import ru.mclient.network.agreement.domain.AgreementEntity

interface UserAgreementService {

    fun getUserAgreement(): AgreementEntity

    fun updateUserAgreement(title: String, content: String): AgreementEntity

    fun getClientDataProcessingAgreement(): AgreementEntity

    fun updateClientDataProcessingAgreement(title: String, content: String): AgreementEntity

}