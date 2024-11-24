package ru.itmo.order.common

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import ru.itmo.order.common.config.TestContainersConfiguration
import ru.itmo.order.OrderApplication

@Transactional
@SpringBootTest(classes = [OrderApplication::class])
@Import(TestContainersConfiguration::class)
abstract class AbstractDatabaseTest