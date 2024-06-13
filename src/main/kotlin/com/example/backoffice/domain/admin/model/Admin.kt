package com.example.backoffice.domain.admin.model

import jakarta.persistence.*

@Entity
@Table(name = "admin")
class Admin(
    @Column(name = "account_id", nullable = false)
    val accountId: String,

    @Column(name = "password", nullable = false)
    val password: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        fun of(accountId: String, password: String): Admin {
            return Admin(accountId = accountId, password = password)
        }
    }
}