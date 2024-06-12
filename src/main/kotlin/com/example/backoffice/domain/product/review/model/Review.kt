package com.example.backoffice.domain.product.review.model

import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "review")
class Review(
    @Column(name = "comment")
    var comment: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "rating")
    var rating: Rating,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,

    @ManyToOne
    val user: User,

    @ManyToOne
    val product: Product,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}