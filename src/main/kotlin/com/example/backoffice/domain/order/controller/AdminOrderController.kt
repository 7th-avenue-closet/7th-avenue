package com.example.backoffice.domain.order.controller

import com.example.backoffice.domain.order.dto.OrderDetailsResponse
import com.example.backoffice.domain.order.dto.OrderStatusRequest
import com.example.backoffice.domain.order.dto.OrdersResponse
import com.example.backoffice.domain.order.service.OrderService
import com.example.backoffice.infra.security.CustomPreAuthorize
import com.example.backoffice.infra.security.MemberPrincipal
import com.example.backoffice.infra.security.MemberRole
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/orders")
class AdminOrderController(
    private val orderService: OrderService,
    private val preAuthorize: CustomPreAuthorize
) {

    @GetMapping("/{orderId}")
    fun getOrder(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable orderId: Long
    ): ResponseEntity<OrderDetailsResponse> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        ResponseEntity.status(HttpStatus.OK).body(
            orderService.getOrderDetailsAdmin(orderId)
        )
    }

    @GetMapping("")
    fun getOrders(
        @AuthenticationPrincipal principal: MemberPrincipal
    ): ResponseEntity<OrdersResponse> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        ResponseEntity.status(HttpStatus.OK).body(
            orderService.getOrdersAdmin()
        )
    }

    @PatchMapping("/{orderId}")
    fun updateOrderStatus(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable orderId: Long,
        @RequestBody orderStatusRequest: OrderStatusRequest
    ): ResponseEntity<Unit> = preAuthorize.hasAnyRole(principal, setOf(MemberRole.ADMIN)) {
        ResponseEntity.status(HttpStatus.OK).body(
            orderService.updateOrderStatus(orderId, orderStatusRequest)
        )
    }

}