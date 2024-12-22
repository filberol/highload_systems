package ru.itmo.files.asyncapi

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import ru.itmo.files.asyncapi.model.OrderUpdateEvent

@Component
@KafkaListener(
    topics = ["\${spring.kafka.topics.order-update-in.name}"],
    groupId = "\${spring.kafka.topics.order-update-in.group-id}"
)
class OrderListener(
    private val messagingTemplate: SimpMessagingTemplate,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(OrderListener::class.java)

    @KafkaHandler
    fun listen(@Payload message: String) {
        try {
            val event = parseMessage(message, OrderUpdateEvent::class.java)
            val notification =
                "Order for user with id ${event.userId} in the status ${event.status}"
            send(notification)
        } catch (e: JsonProcessingException) {
            logger.error("Failed parse Kafka message: $message", e)
        }
    }

    protected fun <T> parseMessage(message: String, targetType: Class<T>): T {
        val parsedMessage: T = objectMapper.readValue(message, targetType)
        logger.info("Received message from Kafka: $parsedMessage")
        return parsedMessage
    }

    protected fun send(notification: String) {
        messagingTemplate.convertAndSend("/topic/order", notification)
        logger.info("Send notification to /topic/order : $notification")
    }
}