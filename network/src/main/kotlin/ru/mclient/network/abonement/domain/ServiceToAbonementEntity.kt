package ru.mclient.network.abonement.domain

import ru.mclient.network.service.domain.ServiceEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "service_to_abonement")
class ServiceToAbonementEntity(
    @ManyToOne
    @JoinColumn
    var service: ServiceEntity,
    @ManyToOne
    @JoinColumn
    var abonement: AbonementEntity,
    @GeneratedValue
    @Id
    var id: Long = 0
)