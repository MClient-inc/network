package ru.mclient.network.service.domain

import javax.persistence.*


@Entity
@Table(name = "service")
class ServiceEntity(
    var title: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var category: ServiceCategoryEntity,
    @Id
    @GeneratedValue
    val id: Long = 0,
)

@Entity
@Table(name = "service_to_company")
class ServiceToCompanyEntity(
    @JoinColumn
    @ManyToOne
    var service: ServiceEntity,
    @JoinColumn
    @ManyToOne
    var categoryToCompany: ServiceCategoryToCompanyEntity,
    @Id
    @GeneratedValue
    var id: Long = 0,
)
