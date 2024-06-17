package com.example.backoffice.domain.product.controller

import com.example.backoffice.domain.product.dto.*
import com.example.backoffice.domain.product.model.Category
import com.example.backoffice.domain.product.service.ProductService
import com.example.backoffice.infra.security.CustomPreAuthorize
import com.example.backoffice.infra.security.MemberPrincipal
import com.example.backoffice.infra.security.MemberRole
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
    private val preAuthorize: CustomPreAuthorize,
) {

    @GetMapping
    fun getProducts(
        @RequestParam pageSize: Long = 10,
        @RequestParam sorted: String = "id.desc",
        @RequestParam cursor: String?,
        @RequestParam category: Category?,
        @RequestParam name: String?,
        @RequestParam onDiscount: Boolean?,
    ): ResponseEntity<List<ProductResponseDto>> {
        val cursorValue: Any? = when (sorted.split(".")[0]) {
            "price", "id" -> cursor?.toLongOrNull()
            "name" -> cursor
            else -> throw IllegalArgumentException("Invalid sorted field")
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.getProducts(pageSize, sorted, cursorValue, category, name, onDiscount))
    }

    @GetMapping("/{productId}")
    fun getProduct(
        @PathVariable("productId") productId: Long,
        @RequestParam cursor: Long
    ): ResponseEntity<ProductDetailResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(productId, cursor))
    }

}