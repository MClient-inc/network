package ru.mclient.network.branch.domain

import ru.mclient.network.network.domain.CompanyNetworkEntity
import javax.persistence.*

@Entity
@Table(name = "company_branches")
class CompanyBranchEntity(
    @Column(name = "title", length = 64, nullable = false)
    var title: String,
    @Column(name = "codename", length = 32, unique = true, nullable = false)
    var codename: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var network: CompanyNetworkEntity,
    @OneToOne(mappedBy = "branch", fetch = FetchType.LAZY)
    var disable: CompanyDisableEntity? = null,
    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long = 0,
)

@Entity
@Table(name = "company_branch_disables")
class CompanyDisableEntity(
    @OneToOne
    @JoinColumn(nullable = false)
    var branch: CompanyBranchEntity,
    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long = 0,
)