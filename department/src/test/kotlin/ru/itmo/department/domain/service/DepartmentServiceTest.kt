package ru.itmo.department.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import reactor.test.test
import ru.itmo.department.api.dto.CheckInResponse
import ru.itmo.department.api.dto.DepartmentResponse
import ru.itmo.department.common.AbstractDatabaseTest
import java.time.OffsetDateTime
import java.util.*


class DepartmentServiceTest : AbstractDatabaseTest() {

    @Autowired
    private lateinit var sut: DepartmentService

    @Test
    fun getDepartments_shouldInvokeRepository() {
        val department = DepartmentResponse(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            name = "department",
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )

        // when
        val result = sut.getDepartments()

        // then
        StepVerifier.create(result)
            .expectNextCount(1)
            .verifyComplete();
    }
//
//    @Test
//    fun checkIn_shouldInvokeRepositoryAndReturnResult() {
//        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
//        val userId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
//        val expected = CheckInResponse(
//            departmentId = departmentId,
//            roomId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
//            personCount = 2L
//        )
//
//        // when
//        val result = sut.checkIn(departmentId, userId)
//
//        // then
//        StepVerifier.create(result)
//            .expectNextCount(1)
//            .verifyComplete()
//
//    }
}