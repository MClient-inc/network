package ru.mclient.network.service.domain

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.network.domain.CompanyNetworkEntity
import javax.persistence.*

@Entity
@Table(name = "service_categories_for_network")
class ServiceCategoryEntity(
    @Column
    var title: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var network: CompanyNetworkEntity,
    @Id
    @GeneratedValue
    var id: Long = 0,
)

@Entity
@Table(name = "service_categories_for_company")
class ServiceCategoryToCompanyEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var company: CompanyBranchEntity,
    @ManyToOne
    @JoinColumn
    var category: ServiceCategoryEntity,
    @Id
    @GeneratedValue
    var id: Long = 0,
)