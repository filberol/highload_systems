package ru.itmo.highload_systems.common

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import ru.itmo.highload_systems.HighloadSystemsApplication


@ContextConfiguration(classes = [HighloadSystemsApplication::class])
@Import(
    value = [
        ObjectMapper::class
    ]
)
abstract class AbstractMvcTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: com.fasterxml.jackson.databind.ObjectMapper
}

