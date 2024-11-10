package ru.itmo.highload_systems.infra.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.test.context.jdbc.Sql
import ru.itmo.highload_systems.common.AbstractDatabaseTest
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.model.Room
import ru.itmo.highload_systems.infra.model.RoomNorm
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
            INSERT INTO oxygen_storage (id, size, capacity, department_id, created_at, updated_at)
            VALUES ('343333fd-05b3-4230-34e6-f26f3fd5c054', 100, 200, '343333fd-05b3-4230-34e6-f26f3fd5c054', '2024-01-01 11:00:00.000000 +00:00', '2024-01-01 11:00:00.000000 +00:00');
        """, """
            INSERT INTO room_norm(id, size, avg_person_norm, created_at, updated_at)
            VALUES ('343333fd-05b3-4230-34e6-f26f3fd5c054', 333, 34.3, '2024-01-01 11:00:00.000000 +00:00', '2024-01-01 11:00:00.000000 +00:00'),
                   ('343333fd-05b3-4230-34e6-f26f3fd5c055', 222, 34.3, '2024-01-01 11:00:00.000000 +00:00', '2024-01-01 11:00:00.000000 +00:00');
        """, """
            INSERT INTO room (id, number, capacity, department_id, room_norm_id, created_at, updated_at)
            VALUES ('343333fd-05b3-4230-34e6-f26f3fd5c054', 555, '666', '343333fd-05b3-4230-34e6-f26f3fd5c054', '343333fd-05b3-4230-34e6-f26f3fd5c054', '2024-01-01 11:00:00.000000 +00:00', '2024-01-01 11:00:00.000000 +00:00'),
                   ('343333fd-05b3-4230-34e6-f26f3fd5c055', 555, '666', '343333fd-05b3-4230-34e6-f26f3fd5c054', '343333fd-05b3-4230-34e6-f26f3fd5c055', '2024-01-01 11:00:00.000000 +00:00', '2024-01-01 11:00:00.000000 +00:00');
        """
        ]
    )
    fun findAllByDepartmentId_shouldReturnResult() {
        val departmentId = UUID.fromString("343333fd-05b3-4230-34e6-f26f3fd5c054")
        val department = Department(
            id = departmentId,
            name = "Department",
            oxygenStorages = listOf(),
            rooms = listOf(),
            createdAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00")
        )
        val roomNorm = RoomNorm(
            id = UUID.randomUUID(),
            size = 5L,
            avgPersonNorm = 50.1,
            createdAt = OffsetDateTime.now()
        )
        val room1 = Room(
            id = UUID.fromString("343333fd-05b3-4230-34e6-f26f3fd5c054"),
            number = 555,
            capacity = 666,
            orders = listOf(),
            roomNorm = roomNorm,
            department = department,
            createdAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00")
        )
        val room2 = Room(
            id = UUID.fromString("343333fd-05b3-4230-34e6-f26f3fd5c055"),
            number = 555,
            capacity = 666,
            orders = listOf(),
            roomNorm = roomNorm,
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
            .ignoringFields("department", "orders", "roomNorm").isEqualTo(expected)
    }
}