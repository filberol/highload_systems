package ru.itmo.oxygen.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.test.context.jdbc.Sql
import ru.itmo.oxygen.api.dto.OxygenStorageResponse
import ru.itmo.oxygen.common.AbstractDatabaseTest
import ru.itmo.oxygen.infra.repository.OxygenStorageRepository
import java.time.OffsetDateTime
import java.util.*

class OxygenStorageServiceTest : AbstractDatabaseTest() {

    @Autowired
    private lateinit var sut: OxygenStorageService

    @Autowired
    private lateinit var oxygenStorageRepository: OxygenStorageRepository

    @Test
    @Sql(
        statements = ["""
          DELETE FROM oxygen_storage;
          """, """  
          INSERT INTO oxygen_storage (id, size, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun findById_shouldInvokeRepository() {
        val id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")

        // when
        val result = sut.findById(id)
        val expected = OxygenStorageResponse(
            id = id,
            size = 5L,
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )

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
    @Sql(
        statements = ["""
          DELETE FROM oxygen_storage;
          """, """  
          INSERT INTO oxygen_storage (id, size, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun findById_shouldThrowException() {
        val id = UUID.randomUUID()
        assertThrows<NoSuchElementException> { sut.findById(id) }
    }

    @Test
    @Sql(
        statements = ["""
          DELETE FROM oxygen_storage;
          """, """  
          INSERT INTO oxygen_storage (id, size, created_at, updated_at)
          VALUES ('20006109-1144-4aa6-8fbf-f45435264de5', '5', '2024-01-03T07:00:00.000000+00:00', '2024-01-03T07:00:00.000000+00:00');
          """
        ]
    )
    fun findAll_shouldInvokeRepository() {
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )

        // when
        val result = sut.findAll(pageable)
        val expected = OxygenStorageResponse(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            size = 5L,
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )

        // then
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(PageImpl(listOf(expected), pageable, 1))
    }

    @Test
    fun create_shouldInvokeRepository() {
        // when
        val result = sut.create(5L)
        // then
        assertThat(result).isNotNull
        assertThat(result.size).isEqualTo(5L)

        val saved = oxygenStorageRepository.findById(result.id)
        assertThat(saved).isNotNull
    }
}