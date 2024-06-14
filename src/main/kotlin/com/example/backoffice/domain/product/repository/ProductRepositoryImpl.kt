package com.example.backoffice.domain.product.repository

import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.model.QProduct
import com.example.backoffice.domain.product.model.Status
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
        name: String?,
        onDiscount: Boolean?
    ): List<Product> {
        val cursored = product.id.max().toString().toLong() - cursor

        val builder = BooleanBuilder()
        builder.and(product.deletedAt.isNull())
        builder.and(product.stock.gt(0))
        category?.let { builder.and(product.category.eq(it)) }
        name?.let { builder.and(product.name.contains(it)) }

        if (sorted != "id.asc") cursored.let { builder.and(product.id.gt(it)) }
        else cursored.let { builder.and(product.id.lt(it)) }
        if (onDiscount == true) builder.and(product.status.eq(Status.ON_DISCOUNT))

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
            "id.desc" -> product.id.desc()
            "id.asc" -> product.id.asc()
            else -> throw IllegalArgumentException("ColumnSorted '$column' is unknown")
        }
    }

}