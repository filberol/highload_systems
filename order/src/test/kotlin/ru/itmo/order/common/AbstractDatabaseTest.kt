package ru.itmo.order.common

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import ru.itmo.order.OrderApplication
import ru.itmo.order.common.config.TestContainersConfiguration

@Transactional
@SpringBootTest(classes = [OrderApplication::class])
@ContextConfiguration(classes = [TestContainersConfiguration::class])
abstract class AbstractDatabaseTest