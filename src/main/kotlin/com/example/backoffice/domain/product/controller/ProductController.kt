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
        @RequestParam sorted: String?,
        @RequestParam cursor: Long = 10,
        @RequestParam category: Category?
    ): ResponseEntity<List<ProductResponseDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts(pageSize, sorted, cursor, category))
    }

    @GetMapping("/{productId}")
    fun getProduct(@PathVariable("productId") productId: Long): ResponseEntity<ProductDetailResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(productId))
    }

    @PostMapping
    fun createProduct(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @RequestBody request: CreateProductRequestDto,
    ): ResponseEntity<IdResponseDto> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request))
    }

    @PutMapping("/{productId}")
    fun updateProduct(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable("productId") productId: Long,
        @RequestBody request: UpdateProductRequestDto,
    ): ResponseEntity<IdResponseDto> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        ResponseEntity.status(HttpStatus.CREATED).body(productService.updateProduct(productId, request))
    }

    @DeleteMapping("/{productId}")
    fun deletePost(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable("productId") productId: Long,
    ): ResponseEntity<Unit> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        ResponseEntity.status(HttpStatus.OK).body(productService.deleteProduct(productId))
    }
}