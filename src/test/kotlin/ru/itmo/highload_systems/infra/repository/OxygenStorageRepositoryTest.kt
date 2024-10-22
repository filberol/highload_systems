package ru.itmo.highload_systems.infra.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import ru.itmo.highload_systems.common.AbstractDatabaseTest
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.model.OxygenStorage
import java.time.OffsetDateTime
import java.util.UUID

class OxygenStorageRepositoryTest : AbstractDatabaseTest() {
    @Autowired
    private lateinit var sut: OxygenStorageRepository


    @Test
    @Sql(
        statements = ["""
            INSERT INTO department (id, name, created_at) 
            VALUES ('343333fd-05b3-4230-34e6-f26f3fd5c054', 'Department', '2024-01-01 11:00:00.000000 +00:00');
        """, """
            INSERT INTO oxygen_storage (id, size, capacity, department_id, created_at, updated_at)
            VALUES ('343333fd-05b3-4230-34e6-f26f3fd5c054', 100, 200, '343333fd-05b3-4230-34e6-f26f3fd5c054', '2024-01-01 11:00:00.000000 +00:00', '2024-01-01 11:00:00.000000 +00:00');
        """
        ]
    )
    fun findByDepartmentId_shouldReturnResult() {
        val departmentId = UUID.fromString("343333fd-05b3-4230-34e6-f26f3fd5c054")
        var department = Department(
            id = departmentId,
            name = "Department",
            oxygenStorages = listOf(),
            rooms = listOf(),
            createdAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00")
        )
        val expected = OxygenStorage(
            id = UUID.fromString("343333fd-05b3-4230-34e6-f26f3fd5c054"),
            size = 100L,
            capacity = 200L,
            department = department,
            createdAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-01T11:00:00.000000+00:00")
        )

        val result = sut.findByDepartmentId(departmentId)
        assertThat(result).isPresent.get()
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .withEqualsForType(OffsetDateTime::isEqual, OffsetDateTime::class.java)
            .ignoringFields("department.oxygenStorages", "department.rooms")
            .isEqualTo(expected)
    }
}