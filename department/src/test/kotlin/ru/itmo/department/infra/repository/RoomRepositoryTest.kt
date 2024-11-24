package ru.itmo.department.infra.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.test.context.jdbc.Sql
import ru.itmo.department.common.AbstractDatabaseTest
import ru.itmo.department.infra.model.Department
import ru.itmo.department.infra.model.Room
import java.time.OffsetDateTime
import java.util.*

class RoomRepositoryTest : AbstractDatabaseTest() {
    @Autowired
    private lateinit var sut: RoomRepository

    @Test
    @Sql(
        statements = ["""
            INSERT INTO department (id, name, created_at) 
            VALUES ('343333fd-05b3-4230-34e6-f26f3fd5c054', 'Department', '2024-01-01 11:00:00.000000 +00:00'),
                   ('343333fd-05b3-4230-34e6-f26f3fd5c055', 'Department1', '2024-01-01 11:00:00.000000 +00:00');
        """, """
            INSERT INTO room (id, capacity, department_id, created_at, updated_at)
            VALUES ('343333fd-05b3-4230-34e6-f26f3fd5c054', '666', '343333fd-05b3-4230-34e6-f26f3fd5c054', '2024-01-01 11:00:00.000000 +00:00', '2024-01-01 11:00:00.000000 +00:00'),
                   ('343333fd-05b3-4230-34e6-f26f3fd5c055', '666', '343333fd-05b3-4230-34e6-f26f3fd5c054', '2024-01-01 11:00:00.000000 +00:00', '2024-01-01 11:00:00.000000 +00:00');
        """, """
            INSERT INTO room_norm(id, size, room_id, people_count, created_at, updated_at)
            VALUES ('343333fd-05b3-4230-34e6-f26f3fd5c054', 333, '343333fd-05b3-4230-34e6-f26f3fd5c054', 34, '2024-01-01 11:00:00.000000 +00:00', '2024-01-01 11:00:00.000000 +00:00'),
                   ('343333fd-05b3-4230-34e6-f26f3fd5c055', 222, '343333fd-05b3-4230-34e6-f26f3fd5c055', 34, '2024-01-01 11:00:00.000000 +00:00', '2024-01-01 11:00:00.000000 +00:00');
        """
        ]
    )
    fun findAllByDepartmentId_shouldReturnResult() {
        val departmentId = UUID.fromString("343333fd-05b3-4230-34e6-f26f3fd5c054")
        val department = Department(
            id = departmentId,
            name = "Department",
            rooms = listOf(),
            createdAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00")
        )
        val room1 = Room(
            id = UUID.fromString("343333fd-05b3-4230-34e6-f26f3fd5c054"),
            capacity = 666,
            department = department,
            createdAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00")
        )
        val room2 = Room(
            id = UUID.fromString("343333fd-05b3-4230-34e6-f26f3fd5c055"),
            capacity = 666,
            department = department,
            createdAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00")
        )
        val pageable = PageRequest.of(
            0, 50, Sort.by(Direction.ASC, "id")
        )
        val result = sut.findAllByDepartmentId(departmentId, pageable)
        val expected = PageImpl(listOf(room1, room2), pageable, 1)
        assertThat(result.get().toList()).usingRecursiveComparison().ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .withEqualsForType(OffsetDateTime::isEqual, OffsetDateTime::class.java)
            .ignoringFields("department", "roomNorm").isEqualTo(expected)
    }
}