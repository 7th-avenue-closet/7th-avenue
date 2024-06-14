package com.example.backoffice.domain.product.repository

import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.model.QProduct
import com.example.backoffice.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.OrderSpecifier
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl : QueryDslSupport(), CustomProductRepository {

    private val product = QProduct.product


    override fun findByPageableAndDeleted(
        pageSize: Long,
        sorted: String?,
        cursor: Long,
        category: Category?,
        name: String?
    ): List<Product> {
        val builder = BooleanBuilder()
        builder.and(product.deletedAt.isNull())
        cursor.let { builder.and(product.id.gt(it)) }
        category?.let { builder.and(product.category.eq(it)) }
        name?.let { builder.and(product.name.contains(it)) }
        builder.and(product.stock.gt(0))

        val sort = getOrderSpecifier(sorted)

        val contents = queryFactory.selectFrom(product)
            .where(builder)
            .orderBy(sort)
            .limit(pageSize)
            .fetch()

        return contents
    }

    private fun getOrderSpecifier(column: String?): OrderSpecifier<*> {
        return when (column) {
            "price.desc" -> product.price.desc()
            "price.asc" -> product.price.asc()
            "name.desc" -> product.name.desc()
            "name.asc" -> product.name.asc()
            else -> product.id.desc()
        }
    }

}