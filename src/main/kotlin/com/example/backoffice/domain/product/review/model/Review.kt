package com.example.backoffice.domain.product.review.model

import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.user.model.User
import jakarta.persistence.*
import java.io.InvalidObjectException
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity
@Table(name = "review")
class Review(
    @Column(name = "comment") var comment: String,

    @Enumerated(EnumType.STRING) @Column(name = "rating") var rating: Rating,

    @Column(name = "image_url") var imageUrl: String?,

    @Column(name = "created_at") val createdAt: ZonedDateTime,

    @Column(name = "updated_at") var updatedAt: ZonedDateTime,

    @Column(name = "deleted_at") var deletedAt: ZonedDateTime? = null,

    @ManyToOne val user: User,

    @ManyToOne val product: Product,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        private fun checkCommentLength(newComment: String) {
            if (newComment.isEmpty() || newComment.length > 200) {
                throw InvalidObjectException("Please write your review with a minimum of 1 character and a maximum of 200 characters.")
            }
        }

        fun of(comment: String, rating: Rating, imageUrl: String?, user: User, product: Product): Review {
            checkCommentLength(comment)
            val timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            return Review(
                comment = comment,
                rating = rating,
                imageUrl = imageUrl,
                user = user,
                product = product,
                createdAt = timestamp,
                updatedAt = timestamp,
            )
        }
    }

    fun updateReview(
        comment: String, rating: String, imageUrl: String?
    ) {
        checkCommentLength(comment)
        this.comment = comment
        this.rating = Rating.fromString(rating)
        this.imageUrl = imageUrl
    }

    fun softDelete() {
        this.deletedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
    }
}