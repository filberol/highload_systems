package ru.itmo.department.domain.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier
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
            createdAt = OffsetDateTime.parse("2024-01-03T10:00+03:00")
        )

        // when
        val result = sut.getDepartments()

        // then
        StepVerifier.create(result)
            .expectNext(department)
            .verifyComplete();
    }
}