package ru.mclient.network.account.domain

import javax.persistence.*

@Entity
@Table(name = "accounts")
class MClientAccountEntity(
    @Column(name = "name", length = 100, nullable = false)
    var name: String,
    @Column(name = "username", length = 32, nullable = false, unique = true, updatable = false)
    var username: String,
    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long = 0,
)
