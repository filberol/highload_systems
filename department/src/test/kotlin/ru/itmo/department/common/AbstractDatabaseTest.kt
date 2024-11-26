package ru.itmo.department.common

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import ru.itmo.department.common.config.TestContainersConfiguration

@SpringBootTest
@Import(value = [TestContainersConfiguration::class])
abstract class AbstractDatabaseTest