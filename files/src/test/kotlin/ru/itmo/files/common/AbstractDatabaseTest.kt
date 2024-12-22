package ru.itmo.files.common

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import ru.itmo.files.FileApplication
import ru.itmo.order.common.config.TestContainersConfiguration

@Transactional
@SpringBootTest(classes = [FileApplication::class])
@ContextConfiguration(classes = [TestContainersConfiguration::class])
abstract class AbstractDatabaseTest