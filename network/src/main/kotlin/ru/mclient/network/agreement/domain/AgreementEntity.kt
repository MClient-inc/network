package ru.mclient.network.agreement.domain

import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user_agreement")
class AgreementEntity(
    @Column(length = 255)
    var title: String,
    @Lob
    var content: String,
    var type: Type,
    @Id
    @GeneratedValue
    var id: Long = 0,
) {

    @CreatedDate
    var createdDate: LocalDateTime = LocalDateTime.MIN


    enum class Type {
        USER_AGREEMENT, CLIENT_DATA_PROCESSING_AGREEMENT,
    }
}