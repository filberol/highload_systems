package ru.itmo.order.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import ru.itmo.order.OrderApplication

@ContextConfiguration(classes = [OrderApplication::class])
@Import(
    value = [
        ObjectMapper::class
    ]
)
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
abstract class AbstractMvcTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: com.fasterxml.jackson.databind.ObjectMapper
}
