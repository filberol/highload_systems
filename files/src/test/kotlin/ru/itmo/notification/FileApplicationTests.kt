package ru.itmo.notification

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import ru.itmo.notification.common.config.TestContainersConfiguration

@ContextConfiguration(classes = [TestContainersConfiguration::class])
@SpringBootTest(classes = [FileApplicationTests::class])
class FileApplicationTests {

    @Test
    fun contextLoads() {
    }

}
