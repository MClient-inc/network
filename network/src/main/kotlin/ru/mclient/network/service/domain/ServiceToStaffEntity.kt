package ru.mclient.network.service.domain

import ru.mclient.network.staff.domain.StaffEntity
import javax.persistence.*

@Entity
@Table(name = "service_to_staff")
class ServiceToStaffEntity(
    @ManyToOne
    @JoinColumn
    var service: ServiceToCompanyEntity,
    @ManyToOne
    @JoinColumn
    var staff: StaffEntity,
    @Id
    @GeneratedValue
    var id: Long = 0,
)