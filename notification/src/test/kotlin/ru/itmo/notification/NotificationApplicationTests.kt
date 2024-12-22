package ru.itmo.notification

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import ru.itmo.order.common.config.TestContainersConfiguration

@ContextConfiguration(classes = [TestContainersConfiguration::class])
@SpringBootTest(classes = [NotificationApplicationTests::class])
class NotificationApplicationTests {

    @Test
    fun contextLoads() {
    }

}
