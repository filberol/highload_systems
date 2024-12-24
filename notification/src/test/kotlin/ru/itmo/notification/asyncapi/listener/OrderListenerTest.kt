package ru.itmo.notification.asyncapi.listener

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.messaging.simp.SimpMessagingTemplate
import ru.itmo.notification.asyncapi.OrderListener
import ru.itmo.notification.asyncapi.model.OrderStatus
import ru.itmo.notification.asyncapi.model.OrderUpdateEvent
import java.util.*

class OrderListenerTest {
    private val messagingTemplate: SimpMessagingTemplate = mockk(relaxed = true)
    private val objectMapper: ObjectMapper = mockk(relaxed = true)
    private var orderListener = OrderListener(messagingTemplate, objectMapper)


    @Test
    fun listen_shouldCorrectlySend() {
        val message = """{"id":"550e8400-e29b-41d4-a716-446655440000","departmentId":"550e8400-e29b-41d4-a716-446655440000",status="NEW}""".trimMargin()
        val event = OrderUpdateEvent(
            UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            null,
            UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            OrderStatus.NEW
        )
        every { objectMapper.readValue(message, OrderUpdateEvent::class.java) } returns event
        orderListener.listen(message)

        verify {
            messagingTemplate.convertAndSend(
                "/topic/order", "Order for user with id ${event.userId} in the status ${event.status}"
            )
        }
    }
}