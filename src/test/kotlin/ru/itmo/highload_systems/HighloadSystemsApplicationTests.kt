package ru.itmo.highload_systems

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import ru.itmo.highload_systems.common.config.TestContainersConfiguration

@Import(TestContainersConfiguration::class)
@SpringBootTest
class HighloadSystemsApplicationTests {

    @Test
    fun contextLoads() {
    }

}
