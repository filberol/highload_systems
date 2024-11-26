package ru.itmo.department.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import reactor.test.StepVerifier
import ru.itmo.department.common.AbstractDatabaseTest
import ru.itmo.department.infra.model.Room
import ru.itmo.department.infra.model.RoomNorm
import ru.itmo.department.infra.repository.RoomNormRepository
import java.util.*

class RoomNormServiceTest : AbstractDatabaseTest() {

    @Autowired
    private lateinit var sut: RoomNormService

    @Autowired
    private lateinit var roomNormRepository: RoomNormRepository


    @Test
    fun save_shouldInvokeRepository() {
        val roomNorm = RoomNorm(
            size = 6L,
            peopleCount = 5L,
            room = Room(id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"))
        )

        // when
        val result = sut.save(roomNorm)

        StepVerifier.create(result).expectNextCount(1)
    }
}