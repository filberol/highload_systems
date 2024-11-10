package ru.itmo.oxygen

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import ru.itmo.highload_systems.common.config.TestContainersConfiguration

@ContextConfiguration(classes = [TestContainersConfiguration::class])
@SpringBootTest(classes = [OxygenServiceApplication::class])
class OxygenServiceApplicationTests {

    @Test
    fun contextLoads() {
    }

}
