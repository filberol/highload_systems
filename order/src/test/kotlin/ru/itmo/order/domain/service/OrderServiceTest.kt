package ru.itmo.order.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import reactor.test.StepVerifier
import ru.itmo.order.api.dto.OrderResponse
import ru.itmo.order.api.dto.OrderStatusRequestResponse
import ru.itmo.order.common.AbstractDatabaseTest
import ru.itmo.order.infra.model.Order
import ru.itmo.order.infra.model.enums.OrderStatus
import ru.itmo.order.infra.repository.OrderRepository
import java.time.OffsetDateTime
import java.util.*

class OrderServiceTest : AbstractDatabaseTest() {

    @Autowired
    private lateinit var sut: OrderService

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Test
    @Sql(
        statements = ["""
          DELETE FROM orders;
          """, """  
          INSERT INTO orders (id, status, department_id, user_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', 'DONE', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun process_shouldThrowException_whenStatusInvalid() {
        val id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")

        // when & then
        StepVerifier.create(sut.process(id, "token"))
            .expectError(IllegalArgumentException::class.java)
            .verify()
    }

    @Test
    @Sql(
        statements = ["""
          DELETE FROM orders;
          """, """  
          INSERT INTO orders (id, status, department_id, user_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', 'NEW', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00'),
                 ('20006109-1144-4aa6-8fbf-f45435264de6', 'DONE', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun cancelExpiredOrders_shouldInvokeRepository() {
        val expiredAt = OffsetDateTime.now()
        val expiredId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val notExpiredId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de6")

        // when & then
        StepVerifier.create(sut.cancelExpiredOrders(expiredAt))
            .expectNextCount(1)
            .verifyComplete()

        val saved = orderRepository.findById(expiredId).orElse(null)
        val notExpired = orderRepository.findById(notExpiredId).orElse(null)

        assertThat(saved.status).isEqualTo(OrderStatus.CANCEL)
        assertThat(notExpired.status).isEqualTo(OrderStatus.DONE)
    }

    @Test
    @Sql(
        statements = ["""
          DELETE FROM orders;
          """, """  
          INSERT INTO orders (id, status, department_id, user_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', 'DONE', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00'),
                 ('20006109-1144-4aa6-8fbf-f45435264de6', 'DONE', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');

          """
        ]
    )
    fun cancelExpiredOrders_shouldReturnEmptyList_whenExpiredOrdersNotExist() {
        val expiredAt = OffsetDateTime.parse("2024-01-02T07:00:00.000000+00:00")

        // when & then
        StepVerifier.create(sut.cancelExpiredOrders(expiredAt))
            .expectNextCount(0)
            .verifyComplete();
    }

    @Test
    @Sql(
        statements = ["""
          DELETE FROM orders;
          """, """  
          INSERT INTO orders (id, status, department_id, user_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', 'NEW', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00'),
                 ('20006109-1144-4aa6-8fbf-f45435264de6', 'DONE', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');

          """
        ]
    )
    fun cancelById_shouldInvokeService() {
        val id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")

        // when
        val result = sut.cancelById(id)

        // when & then
        StepVerifier.create(sut.cancelById(id))
            .expectNextCount(1)
            .verifyComplete()

        val expired = orderRepository.findById(id).orElse(null)
        assertThat(expired.status).isEqualTo(OrderStatus.CANCEL)
    }

    @Test
    @Sql(
        statements = ["""
          INSERT INTO orders (id, status, department_id, user_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', 'DONE', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00'),
                 ('20006109-1144-4aa6-8fbf-f45435264de6', 'DONE', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun findById_shouldInvokeService() {
        val id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val order = Order(
            id = id,
            status = OrderStatus.DONE,
            departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            updatedAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )

        val expected = with(order) {
            OrderResponse(
                id = order.id!!,
                departmentId = departmentId!!,
                status = OrderStatusRequestResponse.DONE,
                userId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
                updatedAt = updatedAt!!,
                createdAt = createdAt!!
            )
        }

        // when & then
        StepVerifier.create(sut.findById(id))
            .expectNext(expected)
            .verifyComplete()
    }
}