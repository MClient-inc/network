package ru.mclient.network.abonement.domain

import ru.mclient.network.network.domain.CompanyNetworkEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name  = "abonements")
class AbonementEntity(
    var title: String,
    @ManyToOne
    @JoinColumn
    var network: CompanyNetworkEntity,
    @OneToMany(cascade = [CascadeType.ALL])
    var subabonements: List<SubabonementEntity> = emptyList(),
    @OneToMany(cascade = [CascadeType.ALL])
    var services: List<ServiceToAbonementEntity> = emptyList(),
    @Id
    @GeneratedValue
    var id: Long = 0,
)

