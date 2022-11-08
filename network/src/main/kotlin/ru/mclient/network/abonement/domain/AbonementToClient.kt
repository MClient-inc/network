package ru.mclient.network.abonement.domain

import org.springframework.data.annotation.CreatedDate
import ru.mclient.network.clients.domain.ClientEntity
import ru.mclient.network.record.domain.RecordAbonementPaymentEntity
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "abonement_to_client")
class AbonementToClientEntity(
    @Column(name = "total_cost")
    var cost: Long,
    @ManyToOne
    @JoinColumn
    var subabonement: SubabonementEntity,
    @ManyToOne
    @JoinColumn
    var client: ClientEntity,
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var payments: List<RecordAbonementPaymentEntity> = emptyList(),
    @GeneratedValue
    @Id
    var id: Long = 0,
) {

    @CreatedDate
    @Column(name = "created_at")
    lateinit var createdAt: LocalDateTime

}




val AbonementToClientEntity.currentUsages get() = payments.size