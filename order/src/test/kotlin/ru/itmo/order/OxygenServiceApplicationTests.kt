package ru.itmo.order

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import ru.itmo.order.common.config.TestContainersConfiguration

@ContextConfiguration(classes = [TestContainersConfiguration::class])
@SpringBootTest(classes = [OrderApplication::class])
class OxygenServiceApplicationTests {

    @Test
    fun contextLoads() {
    }

}
