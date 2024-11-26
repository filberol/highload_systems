package ru.itmo.order.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.order.api.dto.OrderResponse
import ru.itmo.order.clients.DepartmentClient
import ru.itmo.order.clients.UserClient
import ru.itmo.order.clients.dto.CheckInResponse
import ru.itmo.order.clients.exception.InternalServerException
import ru.itmo.order.domain.mapper.OrderApiMapper
import ru.itmo.order.infra.model.Order
import ru.itmo.order.infra.model.enums.OrderStatus
import ru.itmo.order.infra.repository.OrderRepository
import java.time.OffsetDateTime
import java.util.*

@Service
@Transactional(readOnly = true)
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderApiMapper: OrderApiMapper,
    private val userClient: UserClient,
    private val departmentClient: DepartmentClient
) {

    @Transactional(readOnly = false)
    fun create(departmentId: UUID, userId: UUID): Flux<OrderResponse> {
        return Mono.fromCallable { userClient.getById(userId) }.flatMapMany { response ->
            if (response.statusCode.is2xxSuccessful) {
                return@flatMapMany Flux.just(
                    orderApiMapper.toResponse(
                        orderRepository.save(
                            Order(
                                departmentId = departmentId,
                                userId = userId
                            )
                        )
                    )
                )
            } else {
                return@flatMapMany Mono.error(NoSuchElementException("User с id: $userId не найден"))
            }
        }
    }

    @Transactional(readOnly = false)
    fun process(id: UUID): Mono<CheckInResponse> {
        val order = findEntityById(id)
        if (order.status != OrderStatus.NEW) {
            throw IllegalArgumentException(
                "Заявка в статусе %s не может быть обработана".format(
                    order.status
                )
            )
        }
        return departmentClient.checkIn(id, order.userId!!)
            .doOnSuccess {
                order.status = OrderStatus.DONE
                order.onSaveHook()
                orderRepository.save(order)
            }
            .doOnError {
                throw InternalServerException("department not available")
            }
    }

    @Transactional(readOnly = false)
    fun cancelExpiredOrders(
        expiredAt: OffsetDateTime
    ): Mono<List<OrderResponse>> {
        val orders = orderRepository.findAllByUpdatedAtLessThan(expiredAt)
            .stream()
            .filter { order -> OrderStatus.NEW == order.status }
            .map { order ->
                order.status = OrderStatus.CANCEL
                order
            }.toList()
        return Mono.just(orderRepository.saveAll(orders).map(orderApiMapper::toResponse))
    }

    @Transactional(readOnly = false)
    fun cancelById(id: UUID): Flux<OrderResponse> {
        val order = findEntityById(id)
        if (OrderStatus.NEW == order.status) {
            order.status = OrderStatus.CANCEL
            order.onSaveHook()
            return Flux.just(orderApiMapper.toResponse(orderRepository.save(order)))
        }
        throw IllegalArgumentException(
            "Заявка в статусе %s не может быть отменена".format(
                order.status
            )
        )
    }

    fun findAll(pageable: Pageable): Mono<Page<OrderResponse>> {
        return Mono.from { orderRepository.findAll(pageable).map(orderApiMapper::toResponse) }
    }

    fun findById(id: UUID): Flux<OrderResponse> {
        val order = findEntityById(id)
        return Flux.from { orderApiMapper.toResponse(order) }
    }

    private fun findEntityById(id: UUID): Order {
        return orderRepository.findById(id)
            .orElseThrow { NoSuchElementException("Заявка c id %s не найдена".format(id)) }
    }
}