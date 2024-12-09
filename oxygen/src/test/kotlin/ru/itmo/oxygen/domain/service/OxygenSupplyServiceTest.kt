package ru.itmo.oxygen.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import ru.itmo.oxygen.api.dto.OxygenSupplyResponse
import ru.itmo.oxygen.clients.DepartmentClient
import ru.itmo.oxygen.common.AbstractDatabaseTest
import ru.itmo.oxygen.infra.repository.OxygenStorageRepository
import ru.itmo.oxygen.infra.repository.OxygenSupplyRepository
import java.time.OffsetDateTime
import java.util.*

class OxygenSupplyServiceTest : AbstractDatabaseTest() {
    @Autowired
    private lateinit var oxygenSupplyRepository: OxygenSupplyRepository

    @Autowired
    private lateinit var oxygenStorageRepository: OxygenStorageRepository

    @Autowired
    private lateinit var departmentClient: DepartmentClient

    @Autowired
    private lateinit var sut: OxygenSupplyService

    @Test
    fun create_shouldInvokeService() {
        val size = 5L
        val departmentId = UUID.randomUUID()

        // when
        val result = sut.create(size, departmentId)

        // then
        assertThat(result.departmentId).isEqualTo(departmentId)
        assertThat(result.size).isEqualTo(size)

        val saved = oxygenSupplyRepository.findById(result.id)
        assertThat(saved).isNotNull
    }

    @Test
    @Sql(
        statements = ["""
          DELETE FROM oxygen_storage;
          """, """  
          INSERT INTO oxygen_storage (id, size, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '10', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """, """  
          INSERT INTO oxygen_supply (id, size, department_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '4', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun processById_shouldThrowException_whenSupplyNotExist() {
        val id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de6")

        assertThrows<NoSuchElementException> { sut.processById("token", id) }
    }

    @Test
    @Sql(
        statements = ["""
          DELETE FROM oxygen_storage;
          """, """  
          INSERT INTO oxygen_storage (id, size, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '3', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """, """  
          INSERT INTO oxygen_supply (id, size, department_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '4', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun processById_shouldThrowException_whenStorageFull() {
        val id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")

        assertThrows<IllegalArgumentException> { sut.processById("token",id) }

        val storage =
            oxygenStorageRepository.findById(UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"))
                .orElse(null)
        assertThat(storage.size).isEqualTo(3L)
        val supply =
            oxygenSupplyRepository.findById(UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"))
                .orElse(null)
        assertThat(supply.oxygenStorage).isNull()
        assertThat(supply.size).isEqualTo(4L)
    }


    @Test
    @Sql(
        statements = ["""
          DELETE FROM oxygen_storage;
          """, """  
          INSERT INTO oxygen_storage (id, size, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '10', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """, """  
          INSERT INTO oxygen_supply (id, size, department_id, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '4', '20006109-1144-4aa6-8fbf-f45435264de5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun findById_shouldInvokeService() {
        val id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val expected = OxygenSupplyResponse(
            id = id,
            size = 4L,
            departmentId = id,
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
        )

        val result = sut.findById(id)

        // then
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }

    @Test
    fun findById_shouldThrowException() {
        val id = UUID.randomUUID()
        assertThrows<NoSuchElementException> { sut.findById(id) }
    }
}