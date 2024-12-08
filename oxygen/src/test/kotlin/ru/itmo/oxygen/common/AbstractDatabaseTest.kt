package ru.itmo.oxygen.common

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import ru.itmo.oxygen.common.config.TestContainersConfiguration
import ru.itmo.oxygen.OxygenApplication

@Transactional
@SpringBootTest(classes = [OxygenApplication::class])
@Import(TestContainersConfiguration::class)
abstract class AbstractDatabaseTest