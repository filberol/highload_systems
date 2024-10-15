package ru.itmo.highload_systems.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import ru.itmo.highload_systems.HighloadSystemsApplication

@ContextConfiguration(classes = [HighloadSystemsApplication::class])
abstract class AbstractMvcTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc
    @Autowired
    protected lateinit var objectMapper: ObjectMapper
}