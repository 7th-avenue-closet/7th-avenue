package com.example.backoffice.domain.product.review.repository

import com.example.backoffice.domain.product.review.model.QReview
import com.example.backoffice.domain.product.review.model.Review
import com.example.backoffice.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.stereotype.Repository

@Repository
class ReviewRepositoryImpl : CustomReviewRepository, QueryDslSupport() {
    private val review = QReview.review
    override fun getReviews(userId: Long?): List<Review> {

        val getReviewQuery = queryFactory.selectFrom(review)
            .where(review.deletedAt.isNull)

        userId?.let {
            getReviewQuery.where(
                review.user.id.eq(it)
                    .and(review.user.deletedAt.isNull)
            )
        }
        return getReviewQuery.fetch()
    }

    override fun getReviews(cursor: Long?, productId: Long): List<Review> {
        val builder = BooleanBuilder()
        builder.and(review.deletedAt.isNull)
        builder.and(review.product.id.eq(productId))
        cursor?.let { builder.and(review.id.lt(it)) }

        return queryFactory.selectFrom(review)
            .where(builder)
            .limit(10)
            .orderBy(review.id.desc())
            .fetch()
    }
}