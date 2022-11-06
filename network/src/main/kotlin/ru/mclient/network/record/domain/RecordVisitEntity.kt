package ru.mclient.network.record.domain

import ru.mclient.network.abonement.domain.AbonementToClientEntity
import ru.mclient.network.service.domain.ServiceEntity
import javax.persistence.*

@Entity
@Table(name = "record_visit_payment")
class RecordPaymentEntity(
    var type: VisitPaymentMethod,
    var value: Long,
    @ManyToOne
    @JoinColumn
    var record: RecordEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
) {
    enum class VisitPaymentMethod {
        CARD, CASH, ABONEMENT,
    }

}

@Entity
@Table(name = "record_visit_abonement")
class RecordVisitAbonementEntity(
    @ManyToOne
    @JoinColumn
    var service: ServiceEntity,
    @ManyToOne
    @JoinColumn(name = "abonement_id")
    var abonementToClient: AbonementToClientEntity,
    var requiredValue: Long,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    var payment: RecordPaymentEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)