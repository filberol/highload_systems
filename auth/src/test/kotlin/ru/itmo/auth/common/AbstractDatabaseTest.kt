package ru.itmo.auth.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import ru.itmo.auth.AuthApplication
import ru.itmo.auth.common.config.TestContainersConfiguration

@Transactional
@ContextConfiguration(classes = [AuthApplication::class])
@SpringBootTest(classes = [AuthApplication::class])
@Import(
    value = [
        ObjectMapper::class,
        TestContainersConfiguration::class
    ]
)
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
abstract class AbstractDatabaseTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: com.fasterxml.jackson.databind.ObjectMapper
}