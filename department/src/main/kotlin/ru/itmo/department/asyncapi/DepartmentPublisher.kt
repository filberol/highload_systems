package ru.itmo.department.asyncapi

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import ru.itmo.department.asyncapi.model.DepartmentEvent

@Component
class DepartmentPublisher(
    private val kafkaTemplate: KafkaTemplate<String, DepartmentEvent>
) {
    private val logger = LoggerFactory.getLogger(DepartmentPublisher::class.java)

    @Value("\${spring.kafka.topics.department-update-in.name}")
    lateinit var topic: String

    fun send(event: DepartmentEvent) {
        kafkaTemplate.send(topic, event)
        logger.info("Sent to Kafka $topic: $event")
    }
}