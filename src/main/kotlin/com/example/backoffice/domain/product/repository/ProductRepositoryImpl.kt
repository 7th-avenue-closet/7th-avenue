package com.example.backoffice.domain.product.repository

import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.model.Product
import com.example.backoffice.domain.product.model.QProduct
import com.example.backoffice.domain.product.model.Status
import com.example.backoffice.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl : QueryDslSupport(), CustomProductRepository {

    private val product = QProduct.product

    override fun findByPageableAndDeleted(
        pageSize: Long,
        sorted: String,
        cursor: Any?,
        category: Category?,
        name: String?,
        onDiscount: Boolean?,
    ): List<Product> {

        val builder = BooleanBuilder()
        builder.and(product.deletedAt.isNull())
        builder.and(product.stock.gt(0))
        category?.let { builder.and(product.category.eq(it)) }
        name?.let { builder.and(product.name.contains(it)) }

        val (sortField, sortOrder) = sorted.split(".").let { it[0] to it[1] }

        cursor?.let { builder.and(getCursorPredicate(sortField, sortOrder, it)) }

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

    private fun getCursorPredicate(sortField: String, sortOrder: String, cursor: Any): BooleanExpression {
        return when (sortField) {
            "price" -> if (sortOrder == "desc") product.price.lt(cursor as Long) else product.price.gt(cursor as Long)
            "name" -> if (sortOrder == "desc") product.name.lt(cursor as String) else product.name.gt(cursor as String)
            "id" -> if (sortOrder == "desc") product.id.lt(cursor as Long) else product.id.gt(cursor as Long)
            else -> throw IllegalArgumentException("Sort field '$sortField' is unknown")
        }
    }

}