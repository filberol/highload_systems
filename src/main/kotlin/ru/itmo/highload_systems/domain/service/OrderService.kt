package ru.itmo.highload_systems.domain.service

import org.aspectj.weaver.ast.Or
import org.springframework.stereotype.Service
import ru.itmo.highload_systems.api.dto.CreateOrderRequest
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.domain.mapper.OrderApiMapper
import ru.itmo.highload_systems.infra.model.Order
import ru.itmo.highload_systems.infra.model.enums.OrderStatus
import ru.itmo.highload_systems.infra.repository.*
import java.security.InvalidParameterException
import java.time.OffsetDateTime
import java.util.*

@Service
class OrderService(
    private val departmentRepository: DepartmentRepository,
    private val orderRepository: OrderRepository,
    private val orderApiMapper: OrderApiMapper
) {

    fun create(request: CreateOrderRequest): OrderResponse {
        val department = departmentRepository.findById(request.departmentId).orElseThrow()
        val order = orderRepository.save(
            Order(
                status = OrderStatus.NEW,
                size = request.size,
                department = department
            )
        )
        return orderApiMapper.toDto(order)
    }

//    fun process(id: UUID): OrderResponse {
//        val order = orderRepository.findById(id).orElseThrow()
//        if (order.status != OrderStatus.NEW) {
//            throw InvalidParameterException()
//        }
//
//    }
}