package ru.itmo.department.common

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import ru.itmo.department.DepartmentApplication
import ru.itmo.department.common.config.TestContainersConfiguration

@SpringBootTest(classes = [DepartmentApplication::class])
@Import(value = [TestContainersConfiguration::class])
abstract class AbstractDatabaseTest