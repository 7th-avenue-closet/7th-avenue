package com.example.backoffice.domain.user.model

import jakarta.persistence.*
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity
@Table(name = "password_history")
class PasswordHistory(
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "created_at", nullable = false)
    val createdAt: ZonedDateTime,

    @Column(name = "password", nullable = false)
    val password: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        fun of(user: User): PasswordHistory {
            return PasswordHistory(
                user = user,
                createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")),
                password = user.password
            )
        }
    }
}