package ru.mclient.network.clients.domain

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import javax.persistence.*

@Entity
@Table(name = "clients")
class ClientEntity(
    @Column(length = 15)
    var phone: String? = null,
    @Column(length = 64)
    var name: String,
    @JoinColumn
    @ManyToOne
    var network: CompanyNetworkEntity,
    @Id
    @GeneratedValue
    var id: Long = 0,
)

@Entity
@Table(name = "clients_to_company")
class ClientToCompanyEntity(
    @JoinColumn
    @ManyToOne
    var client: ClientEntity,
    @JoinColumn
    @ManyToOne
    var company: CompanyBranchEntity,
    @GeneratedValue
    @Id
    var id: Long = 0,
)