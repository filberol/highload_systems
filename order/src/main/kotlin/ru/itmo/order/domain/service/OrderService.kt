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
    fun process(id: UUID, token: String): Mono<CheckInResponse> {
        return findEntityById(id)
            .map { order -> order.get() }
            .flatMap { order ->
                if (order.status != OrderStatus.NEW) {
                    return@flatMap Mono.error(
                        IllegalArgumentException(
                            "Заявка в статусе ${order.status} не может быть обработана"
                        )
                    )
                }
                departmentClient.checkIn(token, order.departmentId!!, order.userId!!)
                    .let { checkInResponse -> Mono.just(checkInResponse) }
                    .flatMap { checkInResponse ->
                        order.status = OrderStatus.DONE
                        order.onSaveHook()
                        orderRepository.save(order)
                        return@flatMap Mono.just(checkInResponse)
                    }
            }

    }

    @Transactional(readOnly = false)
    fun cancelExpiredOrders(
        expiredAt: OffsetDateTime
    ): Flux<OrderResponse> {
        return Flux.fromIterable(orderRepository.findAllByUpdatedAtLessThan(expiredAt))
            .filter { order -> order.status == OrderStatus.NEW }
            .map { order ->
                order.status = OrderStatus.CANCEL
                order
            }
            .collectList()
            .flatMapMany { orders ->
                Flux.fromIterable(orderRepository.saveAll(orders))
            }
            .map(orderApiMapper::toResponse)
    }

    @Transactional(readOnly = false)
    fun cancelById(id: UUID): Mono<OrderResponse> {
        return findEntityById(id)
            .map { order -> order.get() }
            .handle { order, sink ->
                if (order.status == OrderStatus.NEW) {
                    order.status = OrderStatus.CANCEL
                    val savedOrder = orderRepository.save(order)
                    sink.next(orderApiMapper.toResponse(savedOrder))
                    return@handle
                }
                sink.error(
                    IllegalArgumentException(
                        "Заявка в статусе ${order.status} не может быть отменена"
                    )
                )
            }
    }

    fun findAll(pageable: Pageable): Flux<Page<OrderResponse>> {
        return Flux.just(orderRepository.findAll(pageable))
            .map { it -> it.map { orderApiMapper.toResponse(it) } }
    }

    fun findById(id: UUID): Mono<OrderResponse> {
        return findEntityById(id)
            .map { order -> orderApiMapper.toResponse(order.get()) }
    }

    private fun findEntityById(id: UUID): Mono<Optional<Order>> {
        return Mono.just(id)
            .map(orderRepository::findById)
            .switchIfEmpty(
                Mono.error(NoSuchElementException("Заявка c id %s не найдена".format(id)))
            )
    }
}