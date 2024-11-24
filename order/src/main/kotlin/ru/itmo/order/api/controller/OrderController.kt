package ru.itmo.order.api.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.itmo.order.api.dto.OrderResponse
import ru.itmo.order.domain.service.OrderService
import java.time.OffsetDateTime
import java.util.*

@RestController
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/orders")
    @PreAuthorize("permitAll()")
    fun create(@RequestParam @Valid departmentId: UUID): OrderResponse {
        return orderService.create(departmentId)
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PostMapping("/orders/{id}")
    fun process(@PathVariable @NotNull id: UUID): OrderResponse {
        return orderService.process(id)
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PostMapping("/orders/cancel")
    fun cancel(
        @RequestParam expiredAt: OffsetDateTime
    ): List<OrderResponse> {
        return orderService.cancelExpiredOrders(expiredAt)
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PostMapping("/orders/{id}/cancel")
    fun cancelById(@PathVariable @NotNull id: UUID): OrderResponse {
        return orderService.cancelById(id)
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/orders")
    fun getOrders(
        @PageableDefault(sort = ["id"], size = 50) pageable: Pageable
    ): Page<OrderResponse> {
        return orderService.findAll(pageable)
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @GetMapping("/orders/{id}")
    fun getOrderById(@PathVariable @NotNull id: UUID): OrderResponse {
        return orderService.findById(id)
    }
}