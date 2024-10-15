package ru.itmo.highload_systems.api.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import ru.itmo.highload_systems.api.dto.CreateOrderRequest
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.api.dto.OrderStatusRequestResponse
import ru.itmo.highload_systems.domain.service.OrderService
import java.time.OffsetDateTime
import java.util.*

@RestController
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/orders")
    fun create(@RequestBody @Valid request: CreateOrderRequest): OrderResponse {
        return orderService.create(request)
    }

    @PostMapping("/orders/{id}")
    fun process(@PathVariable @NotNull id: UUID): OrderResponse {
        return orderService.process(id)
    }

    @PostMapping("/orders/cancel")
    fun cancel(
        @RequestParam expiredAt: OffsetDateTime,
        @RequestParam status: OrderStatusRequestResponse
    ): List<OrderResponse> {
        return orderService.cancelExpiredOrders(expiredAt, status)
    }

    @PostMapping("/orders/{id}/cancel")
    fun cancelById(@PathVariable @NotNull id: UUID): OrderResponse {
        return orderService.cancelById(id)
    }

    @GetMapping("/orders")
    fun getOrders(pageable: Pageable): Page<OrderResponse> {
        return orderService.findAll(pageable)
    }

    @GetMapping("/orders/{id}")
    fun getOrderById(@PathVariable @NotNull id: UUID): OrderResponse {
        return orderService.findById(id)
    }
}