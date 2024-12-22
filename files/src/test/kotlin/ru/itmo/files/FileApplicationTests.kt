package ru.itmo.files

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import ru.itmo.order.common.config.TestContainersConfiguration

@ContextConfiguration(classes = [TestContainersConfiguration::class])
@SpringBootTest(classes = [FileApplicationTests::class])
class FileApplicationTests {

    @Test
    fun contextLoads() {
    }

}
