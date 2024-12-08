package ru.itmo.department.infra.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import reactor.test.StepVerifier
import ru.itmo.department.common.AbstractDatabaseTest
import java.util.*

class RoomRepositoryTest : AbstractDatabaseTest() {
    @Autowired
    private lateinit var sut: RoomRepository

    @Test
    fun findByDepartmentId_shouldReturnResult() {
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val pageable = PageRequest.of(
            0, 50, Sort.by(Direction.ASC, "id")
        )
        val result = sut.findByDepartmentId(departmentId, pageable)
        StepVerifier.create(result).expectNextCount(1)

//        val expected = PageImpl(listOf(room1, room2), pageable, 1)
//        assertThat(listOf(result.blockFirst(), result.blockLast())).usingRecursiveComparison()
//            .ignoringAllOverriddenEquals()
//            .ignoringCollectionOrder()
//            .withEqualsForType(OffsetDateTime::isEqual, OffsetDateTime::class.java)
//            .ignoringFields("department", "roomNorm").isEqualTo(expected)
    }
}