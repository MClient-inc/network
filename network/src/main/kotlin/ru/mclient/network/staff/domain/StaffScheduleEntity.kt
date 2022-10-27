package ru.mclient.network.staff.domain

import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "staff_schedule")
class StaffScheduleEntity(
    @Column(name = "date_schedule")
    var date: LocalDate,
    @Column(name = "start_time")
    var from: LocalTime,
    @Column(name = "end_time")
    var to: LocalTime,
    @ManyToOne
    @JoinColumn
    var staff: StaffEntity,
    @Id
    @GeneratedValue
    var id: Long = 0,
)