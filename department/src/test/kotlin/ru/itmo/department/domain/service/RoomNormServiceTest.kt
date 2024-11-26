package ru.itmo.department.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
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
    @Sql(
        statements = ["""  
          INSERT INTO department (id, name, created_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', 'department', '2024-01-03T07:00:00.000000+00:00');
          """, """  
          INSERT INTO room (id, capacity, department_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '40', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """]
    )
    fun save_shouldInvokeRepository() {
        val roomNorm = RoomNorm(
            size = 6L,
            peopleCount = 5L,
            room = Room(id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"))
        )

        // when
        val result = sut.save(roomNorm).block()!!

        // then
        assertThat(result.id).isNotNull
        assertThat(result.updatedAt).isNotNull
        assertThat(result.createdAt).isNotNull

        val saved = roomNormRepository.findById(roomNorm.id!!).block()!!
        assertThat(saved).isNotNull
        assertThat(saved.room!!.id).isEqualTo(UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"))
    }
}