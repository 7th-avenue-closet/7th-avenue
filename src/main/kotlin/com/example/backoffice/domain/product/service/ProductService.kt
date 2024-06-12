package com.example.backoffice.domain.product.service

import com.example.backoffice.domain.product.dto.CreateProductRequestDto
import com.example.backoffice.domain.product.dto.ProductDetailResponseDto
import com.example.backoffice.domain.product.dto.ProductResponseDto
import com.example.backoffice.domain.product.dto.UpdateProductRequestDto
import com.example.backoffice.domain.product.model.*
import com.example.backoffice.domain.product.repository.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime


@Service
class ProductService (
    private val productRepository: ProductRepository,
) {
    fun getProducts(): List<ProductResponseDto> {
        return productRepository.findAll().map { it.toResponse()}
    }

    fun getProductById(productId: Long): ProductDetailResponseDto {
        val product = productRepository.findByIdOrNull(productId) ?: throw RuntimeException("Product with ID $productId not found")
        return product.toDetailResponse()
    }

    @Transactional
    fun createProduct(request: CreateProductRequestDto): ProductResponseDto {


        return productRepository.save(
            Product(
                name = request.name,
                price = request.price,
                description = request.description,
                category = when (request.category) {
                    Category.ACC.name -> Category.ACC
                    Category.TOP.name -> Category.TOP
                    Category.OUTER.name -> Category.OUTER
                    Category.BOTTOM.name -> Category.BOTTOM
                    else -> throw IllegalArgumentException("Unknown category")
                },
                status = status(request.stock, request.discountRate),
                stock = request.stock,
                discountRate = request.discountRate,
                imageUrl = request.imageUrl,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )
        ).toResponse()
    }

    fun updateProduct(productId: Long, request: UpdateProductRequestDto) : ProductResponseDto {
        TODO()
    }

    fun deleteProduct(productId: Long) {
        TODO()
    }

    fun status(stock: Int, discountRate: Int?) : Status {
        return if(stock < 1)  Status.SOLD_OUT
        else if(discountRate!!>0) Status.ON_SALE
        else Status.ON_DISCOUNT
    }

}