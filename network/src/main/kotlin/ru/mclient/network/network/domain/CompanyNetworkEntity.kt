package ru.mclient.network.network.domain

import ru.mclient.network.account.domain.MClientAccountEntity
import ru.mclient.network.branch.domain.CompanyBranchEntity
import javax.persistence.*


@Entity
@Table(name = "company_networks")
class CompanyNetworkEntity(
    @Column(name = "title", length = 64, nullable = false)
    var title: String,
    @Column(name = "codename", length = 32, unique = true, nullable = false)
    var codename: String,
    @OneToOne(mappedBy = "network", fetch = FetchType.LAZY)
    var disable: CompanyNetworkDisableEntity? = null,
    @ManyToOne
    @JoinColumn
    var owner: MClientAccountEntity,
    @OneToMany(fetch = FetchType.LAZY)
    var companies: List<CompanyBranchEntity> = emptyList(),
    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long = 0,
)

@Entity
@Table(name = "company_network_disables")
class CompanyNetworkDisableEntity(
    @OneToOne
    @JoinColumn(nullable = false)
    var network: CompanyNetworkEntity,
    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long = 0,
)