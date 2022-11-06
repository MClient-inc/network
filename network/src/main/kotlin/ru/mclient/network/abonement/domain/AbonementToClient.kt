package ru.mclient.network.abonement.domain

import org.springframework.data.annotation.CreatedDate
import ru.mclient.network.clients.domain.ClientEntity
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "abonement_to_client")
class AbonementToClientEntity(
    @ManyToOne
    @JoinColumn
    var abonement: AbonementEntity,
    @ManyToOne
    @JoinColumn
    var client: ClientEntity,
    @GeneratedValue
    @Id
    var id: Long,
) {


    @CreatedDate
    @Column(name = "created_at")
    lateinit var createdAt: LocalDateTime

}