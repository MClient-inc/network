package ru.mclient.network.record.domain

import ru.mclient.network.abonement.domain.AbonementToClientEntity
import ru.mclient.network.service.domain.ServiceEntity
import javax.persistence.*

@Entity
@DiscriminatorColumn(name = "payment_type")
@Inheritance(strategy = InheritanceType.JOINED)
open class RecordPaymentEntity(
    @Column(name = "pay_value")
    open var value: Long,
    @ManyToOne
    @JoinColumn
    open var record: RecordEntity,
) {
    @GeneratedValue
    @Id
    open var id: Long = 0
}

@Entity
@Table(name = "record_visit_abonement")
class RecordAbonementPaymentEntity(
    value: Long,
    record: RecordEntity,
    @ManyToOne
    @JoinColumn
    var service: ServiceEntity?,
    @ManyToOne
    @JoinColumn(name = "abonement_id")
    var abonementToClient: AbonementToClientEntity,
) : RecordPaymentEntity(value, record)