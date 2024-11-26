package ru.itmo.department.infra.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.test.context.jdbc.Sql
import reactor.test.StepVerifier
import reactor.test.expectError
import ru.itmo.department.common.AbstractDatabaseTest
import ru.itmo.department.infra.model.Department
import ru.itmo.department.infra.model.Room
import java.time.OffsetDateTime
import java.util.*

class RoomRepositoryTest : AbstractDatabaseTest() {
    @Autowired
    private lateinit var sut: RoomRepository

    @Test
    fun findAllByDepartmentId_shouldReturnResult() {
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val pageable = PageRequest.of(
            0, 50, Sort.by(Direction.ASC, "id")
        )
        val result = sut.findAllByDepartmentId(departmentId, pageable)
        StepVerifier.create(result).expectNextCount(1)

//        val expected = PageImpl(listOf(room1, room2), pageable, 1)
//        assertThat(listOf(result.blockFirst(), result.blockLast())).usingRecursiveComparison()
//            .ignoringAllOverriddenEquals()
//            .ignoringCollectionOrder()
//            .withEqualsForType(OffsetDateTime::isEqual, OffsetDateTime::class.java)
//            .ignoringFields("department", "roomNorm").isEqualTo(expected)
    }
}