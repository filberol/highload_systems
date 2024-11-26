package ru.itmo.department.common

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.Transactional
import ru.itmo.department.common.config.TestContainersConfiguration
import ru.itmo.department.DepartmentServiceApplication


@EnableTransactionManagement
@SpringBootTest(classes = [DepartmentServiceApplication::class])
@Import(TestContainersConfiguration::class)
abstract class AbstractDatabaseTest{}