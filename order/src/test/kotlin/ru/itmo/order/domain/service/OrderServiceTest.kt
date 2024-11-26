package ru.itmo.order.domain.service

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.ResponseEntity
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase
import reactor.core.publisher.Mono
import ru.itmo.order.api.dto.OrderResponse
import ru.itmo.order.api.dto.OrderStatusRequestResponse
import ru.itmo.order.clients.DepartmentClient
import ru.itmo.order.clients.UserClient
import ru.itmo.order.clients.dto.CheckInResponse
import ru.itmo.order.clients.dto.RoleRequestResponse
import ru.itmo.order.clients.dto.UserResponse
import ru.itmo.order.common.AbstractDatabaseTest
import ru.itmo.order.infra.model.Order
import ru.itmo.order.infra.model.enums.OrderStatus
import ru.itmo.order.infra.repository.OrderRepository
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Import(value = [OrderServiceTest.OrderServiceTestConfig::class])
class OrderServiceTest : AbstractDatabaseTest() {

    @Autowired
    private lateinit var sut: OrderService

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @MockBean
    private lateinit var departmentClient: DepartmentClient

    @MockBean
    private lateinit var userClient: UserClient

    @TestConfiguration
    internal class OrderServiceTestConfig {
        @Bean
        fun userClient() = mockk<UserClient>()

        @Bean
        fun departmentClient() = mockk<DepartmentClient>()
    }

//    @Test
//    @Sql(
//        statements = ["""
//                      DELETE FROM orders;
//          """], executionPhase = ExecutionPhase.AFTER_TEST_METHOD
//    )
//    fun create_shouldInvokeService() {
//        val departmentId = UUID.randomUUID()
//        val userId = UUID.randomUUID()
//
//        every { userClient.getById(userId) }.returns(
//            ResponseEntity.ok(
//                UserResponse(
//                    id = userId,
//                    name = "name",
//                    login = "login",
//                    password = "password",
//                    role = RoleRequestResponse.USER
//                )
//            )
//        )
//
//        // when
//        val result = sut.create(departmentId, userId).blockFirst()!!
//
//        // then
//        assertEquals(result.departmentId, departmentId)
//        val saved = orderRepository.findById(result.id)
//        assertThat(saved).isPresent
//    }

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
        assertThrows<IllegalArgumentException> { sut.process(id) }
    }

//    @Test
//    @Sql(
//        statements = ["""
//          DELETE FROM orders;
//          """, """
//          INSERT INTO orders (id, status, department_id, user_id, created_at, updated_at)
//          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', 'NEW', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
//          """
//        ]
//    )
//    fun process_shouldInvokeService() {
//        val id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
//        val userId = UUID.randomUUID()
//        every { departmentClient.checkIn(id, userId) }
//            .returns(
//                Mono.just(
//                    CheckInResponse(
//                        departmentId = id,
//                        roomId = UUID.randomUUID(),
//                        personCount = 2L
//                    )
//                )
//            )
//        // when
//        val result = sut.process(id).block()!!
//
//        // then
//        assertThat(result).isNotNull
//    }

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

        // when
        val result = sut.cancelExpiredOrders(expiredAt).block()!!

        // then
        assertThat(result.size).isEqualTo(1)
        val expired = result.first()
        assertThat(expired.id).isEqualTo(expiredId)
        assertThat(expired.status).isEqualTo(OrderStatusRequestResponse.CANCEL)

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

        // when
        val result = sut.cancelExpiredOrders(expiredAt).block()!!

        // then
        assertThat(result).isEmpty()
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
        val result = sut.cancelById(id).blockFirst()!!

        // then
        assertTrue { result.status == OrderStatusRequestResponse.CANCEL }
        val expired = orderRepository.findById(id).orElse(null)
        assertThat(expired.status).isEqualTo(OrderStatus.CANCEL)
        assertThat(result.updatedAt).isAfter(OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"))
    }

//
//    @Test
//    @Sql(
//        statements = ["""
//          INSERT INTO orders (id, status, department_id, user_id, created_at, updated_at)
//          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', 'DONE', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00'),
//                 ('20006109-1144-4aa6-8fbf-f45435264de6', 'DONE', '20006109-1144-4aa6-8fbf-f45435264de5', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
//          """
//        ]
//    )
//    fun findById_shouldInvokeService() {
//        val id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
//        val order = Order(
//            id = id,
//            status = OrderStatus.DONE,
//            departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
//            updatedAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
//            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
//        )
//        // when
//        val result = sut.findById(id)
//
//        // then
//        val expected = with(order) {
//            OrderResponse(
//                id = order.id!!,
//                departmentId = departmentId!!,
//                status = OrderStatusRequestResponse.DONE,
//                userId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
//                updatedAt = updatedAt!!,
//                createdAt = createdAt!!
//            )
//        }
//        assertThat(result.blockFirst())
//            .usingComparatorForType(
//                OffsetDateTimeByInstantComparator.getInstance(),
//                OffsetDateTime::class.java
//            )
//            .usingRecursiveComparison()
//            .isEqualTo(expected)
//    }
}