package com.example.backoffice.domain.user.model

import jakarta.persistence.*

@Entity
@Table(name = "app_user")
class User(
    @Column(name = "account_id", nullable = false, unique = true)
    val accountId: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "image_url")
    var imageUrl: String?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}