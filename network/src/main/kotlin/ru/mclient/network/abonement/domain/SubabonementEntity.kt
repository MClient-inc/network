package ru.mclient.network.abonement.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "subabonements")
class SubabonementEntity(
    var title: String,
    var usages: Int,
    var availableUntil: LocalDateTime,
    @Column(name = "live_time")
    var liveTimeInMillis: Long,
    @ManyToOne
    @JoinColumn
    var abonement: AbonementEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)