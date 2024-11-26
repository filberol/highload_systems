package ru.itmo.department.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.test.context.jdbc.Sql
import reactor.core.publisher.Flux
import ru.itmo.department.api.dto.CheckInResponse
import ru.itmo.department.api.dto.DepartmentResponse
import ru.itmo.department.common.AbstractDatabaseTest
import ru.itmo.department.infra.model.Department
import java.time.OffsetDateTime
import java.util.*

class DepartmentServiceTest : AbstractDatabaseTest() {

    @Autowired
    private lateinit var sut: DepartmentService


    @Test
    @Sql(
        statements = ["""  
          INSERT INTO department (id, name, created_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', 'department', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun getDepartments_shouldInvokeRepository() {
        val department = DepartmentResponse(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            name = "department",
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )

        // when
        val result = sut.getDepartments()

        // then
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(Flux.just(department))
    }

    @Test
    @Sql(
        statements = ["""  
          INSERT INTO department (id, name, created_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', 'department', '2024-01-03T07:00:00.000000+00:00');
          """, """  
          INSERT INTO room (id, capacity, department_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '40', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """, """  
          INSERT INTO room_norm (id, size, people_count, room_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '30', '1', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun checkIn_shouldInvokeRepositoryAndReturnResult() {
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val  userId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val expected = CheckInResponse(
            departmentId = departmentId,
            roomId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            personCount = 2L
        )

        // when
        val result = sut.checkIn(departmentId, userId)

        // then
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(Flux.just(expected))
    }
}