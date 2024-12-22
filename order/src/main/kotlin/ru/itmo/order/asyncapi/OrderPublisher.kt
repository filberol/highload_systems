package ru.itmo.order.asyncapi

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import ru.itmo.order.asyncapi.model.OrderUpdateEvent

@Component
class OrderPublisher(private val kafkaTemplate: KafkaTemplate<String, OrderUpdateEvent>) {

    private val logger = LoggerFactory.getLogger(OrderPublisher::class.java)

    @Value("\${spring.kafka.topics.order-update-in.name}")
    lateinit var topic: String

    fun send(event: OrderUpdateEvent) {
        kafkaTemplate.send(topic, event)
        logger.info("Sent to Kafka $topic: $event")
    }
}