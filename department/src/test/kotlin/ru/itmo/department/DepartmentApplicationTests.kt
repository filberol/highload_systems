package ru.itmo.department

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import ru.itmo.department.common.config.TestContainersConfiguration

@ContextConfiguration(classes = [TestContainersConfiguration::class])
@SpringBootTest(classes = [DepartmentApplication::class])
class DepartmentApplicationTests {

    @Test
    fun contextLoads() {
    }

}
