package ru.itmo.notification.asyncapi.listener

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.messaging.simp.SimpMessagingTemplate
import ru.itmo.notification.asyncapi.DepartmentListener
import ru.itmo.notification.asyncapi.model.DepartmentEvent
import java.util.*

class DepartmentListenerTest {
    private val messagingTemplate: SimpMessagingTemplate = mockk(relaxed = true)
    private val objectMapper: ObjectMapper = mockk(relaxed = true)
    private var departmentListener = DepartmentListener(messagingTemplate, objectMapper)


    @Test
    fun listen_shouldCorrectlySend() {
        val message = """{"departmentId":"550e8400-e29b-41d4-a716-446655440000","size":5}""".trimMargin()
        val event = DepartmentEvent(
            UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), 5
        )
        every { objectMapper.readValue(message, DepartmentEvent::class.java) } returns event
        departmentListener.listen(message)

        verify {
            messagingTemplate.convertAndSend(
                "/topic/department", "Department with id ${event.departmentId} received oxygen with size ${event.size}"
            )
        }
    }
}