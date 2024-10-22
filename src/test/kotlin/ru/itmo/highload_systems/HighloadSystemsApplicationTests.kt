package ru.itmo.highload_systems

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import ru.itmo.highload_systems.common.config.TestContainersConfiguration

@ContextConfiguration(classes = [TestContainersConfiguration::class])
@SpringBootTest(classes = [HighloadSystemsApplication::class])
class HighloadSystemsApplicationTests {

    @Test
    fun contextLoads() {
    }

}
