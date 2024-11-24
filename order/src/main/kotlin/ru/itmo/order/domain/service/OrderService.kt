package ru.itmo.order.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.order.api.dto.OrderResponse
import ru.itmo.order.domain.mapper.OrderApiMapper
import ru.itmo.order.infra.model.Order
import ru.itmo.order.infra.model.enums.OrderStatus
import ru.itmo.order.infra.repository.OrderRepository
import java.security.InvalidParameterException
import java.time.OffsetDateTime
import java.util.*

@Service
@Transactional(readOnly = true)
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderApiMapper: OrderApiMapper
) {

    @Transactional(readOnly = false)
    fun create(departmentId: UUID): OrderResponse {
        val order = orderRepository.save(Order(departmentId = departmentId))
        return orderApiMapper.toResponse(order)
    }

    @Transactional(readOnly = false)
    fun process(id: UUID): OrderResponse {
        val order = findEntityById(id)
        if (order.status != OrderStatus.NEW) {
            throw IllegalArgumentException(
                "Заявка в статусе %s не может быть обработана".format(
                    order.status
                )
            )
        }
        order.status = OrderStatus.DONE
        order.onSaveHook()
        val saved = orderRepository.save(order)
        return orderApiMapper.toResponse(saved)
    }

    @Transactional(readOnly = false)
    fun cancelExpiredOrders(
        expiredAt: OffsetDateTime
    ): List<OrderResponse> {
        val orders = orderRepository.findAllByUpdatedAtLessThan(expiredAt)
            .stream()
            .filter { order -> OrderStatus.NEW == order.status }
            .map { order ->
                order.status = OrderStatus.CANCEL
                order
            }.toList()
        return orderRepository.saveAll(orders).map(orderApiMapper::toResponse)
    }

    @Transactional(readOnly = false)
    fun cancelById(id: UUID): OrderResponse {
        val order = findEntityById(id)
        if (OrderStatus.NEW == order.status) {
            order.status = OrderStatus.CANCEL
            order.onSaveHook()
            return orderApiMapper.toResponse(orderRepository.save(order))
        }
        throw IllegalArgumentException(
            "Заявка в статусе %s не может быть отменена".format(
                order.status
            )
        )
    }

    fun findAll(pageable: Pageable): Page<OrderResponse> {
        return orderRepository.findAll(pageable).map(orderApiMapper::toResponse)
    }

    fun findById(id: UUID): OrderResponse {
        val order = findEntityById(id)
        return orderApiMapper.toResponse(order)
    }

    private fun findEntityById(id: UUID): Order {
        return orderRepository.findById(id)
            .orElseThrow { NoSuchElementException("Заявка c id %s не найдена".format(id)) }
    }
}