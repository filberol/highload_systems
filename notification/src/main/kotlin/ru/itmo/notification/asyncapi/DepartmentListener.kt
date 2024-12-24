package ru.itmo.notification.asyncapi

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import ru.itmo.notification.asyncapi.model.DepartmentEvent

@Component
@KafkaListener(
    topics = ["\${spring.kafka.topics.department-update-in.name}"],
    groupId = "\${spring.kafka.topics.department-update-in.group-id}"
)
class DepartmentListener(
    private val messagingTemplate: SimpMessagingTemplate,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(DepartmentListener::class.java)

    @KafkaHandler
    fun listen(@Payload message: String) {
        try {
            val event = parseMessage(message, DepartmentEvent::class.java)
            val notification =
                "Department with id ${event.departmentId} received oxygen with size ${event.size}"
            send(notification)
        } catch (e: JsonProcessingException) {
            logger.error("Failed parse Kafka message: $message", e)
        }
    }

    private fun <T> parseMessage(message: String, targetType: Class<T>): T {
        val parsedMessage: T = objectMapper.readValue(message, targetType)
        logger.info("Received message from Kafka: $parsedMessage")
        return parsedMessage
    }

    private fun send(notification: String) {
        messagingTemplate.convertAndSend("/topic/department", notification)
        logger.info("Send notification to /topic/department : $notification")
    }
}