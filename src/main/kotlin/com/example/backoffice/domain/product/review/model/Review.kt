package com.example.backoffice.domain.product.review.model

import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.user.model.User
import jakarta.persistence.*
import java.io.InvalidObjectException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity
@Table(name = "review")
class Review(
    @Column(name = "comment")
    var comment: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "rating")
    var rating: Rating,

    @Column(name = "created_at")
    val createdAt: ZonedDateTime,

    @Column(name = "updated_at")
    var updatedAt: ZonedDateTime,

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

    companion object {
        private fun checkCommentLength(newComment: String) {
            if (newComment.isEmpty() || newComment.length > 200) {
                throw InvalidObjectException("리뷰의 내용은 1자 이상 200자 이하로 작성해주세요!")
            }
        }

        fun of(comment: String, rating: Rating, user: User , product: Product): Review {
            checkCommentLength(comment)
            val timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            return Review(
                comment = comment,
                rating = rating,
                user = user,
                product = product,
                createdAt = timestamp,
                updatedAt = timestamp,
                isDeleted = false
            )
        }
    }
}
