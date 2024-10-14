package ru.itmo.highload_systems.common

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems.HighloadSystemsApplication
import ru.itmo.highload_systems.config.TestContainersConfiguration

@Transactional
@SpringBootTest(classes = [HighloadSystemsApplication::class])
@Import(TestContainersConfiguration::class)
abstract class AbstractDatabaseTest