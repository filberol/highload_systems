package ru.itmo.highload_systems

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class HighloadSystemsApplicationTests {

    @Test
    fun contextLoads() {
    }

}
