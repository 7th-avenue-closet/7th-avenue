package com.example.backoffice.domain.product.review.repository

import com.example.backoffice.domain.product.review.model.QReview
import com.example.backoffice.domain.product.review.model.Review
import com.example.backoffice.infra.querydsl.QueryDslSupport
import org.springframework.stereotype.Repository

@Repository
class ReviewRepositoryImpl: CustomReviewRepository, QueryDslSupport() {
    override fun getReviews(userId: Long?): List<Review> {
        val review = QReview.review

        val getReviewQuery = queryFactory.selectFrom(review)
            .where(review.deletedAt.isNull)

        if (userId != null) {
            getReviewQuery.where(review.user.id.eq(userId))
        }
        return getReviewQuery.fetch()
    }
}