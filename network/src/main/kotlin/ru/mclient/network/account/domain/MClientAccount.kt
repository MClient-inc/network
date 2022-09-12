package ru.mclient.network.account.domain

import javax.persistence.*

@Entity
@Table(name = "accounts")
class MClientAccountEntity(
    @OneToOne(mappedBy = "account")
    var data: MClientAccountDataEntity,
    @Column(name = "username", length = 32, nullable = false, unique = true, updatable = false)
    var username: String,
    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long,
)

@Entity
@Table(name = "account_data")
class MClientAccountDataEntity(
    @Column(name = "name", length = 100, nullable = false)
    var name: String,
    @OneToOne
    @JoinColumn(nullable = false)
    @MapsId
    var account: MClientAccountEntity? = null,
    @Id
    @GeneratedValue
    var id: Long,
)