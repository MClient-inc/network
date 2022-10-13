package ru.mclient.network.staff.domain

import ru.mclient.network.branch.domain.CompanyBranchEntity
import javax.persistence.*

@Entity
@Table
class StaffEntity(
    @Column(length = 64)
    var name: String,
    @Column(length = 100)
    var role: String,
    @Column(unique = true, length = 32)
    var codename: String,
    @ManyToOne
    @JoinColumn
    var company: CompanyBranchEntity,
    var layoff: Boolean = false,
    @Id
    @GeneratedValue
    var id: Long = 0,
)