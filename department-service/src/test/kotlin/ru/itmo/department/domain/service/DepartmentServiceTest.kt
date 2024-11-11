package ru.itmo.department.domain.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import ru.itmo.department.api.config.DepartmentConfig.PERSON_OXYGEN_NORM
import ru.itmo.department.api.dto.CheckInResponse
import ru.itmo.department.common.config.DomainMapperTestConfiguration
import ru.itmo.department.infra.model.Department
import ru.itmo.department.infra.model.Room
import ru.itmo.department.infra.model.RoomNorm
import ru.itmo.department.infra.repository.DepartmentRepository
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [DepartmentService::class])
@Import(value = [DomainMapperTestConfiguration::class, DepartmentServiceTest.DepartmentServiceTestConfig::class])
class DepartmentServiceTest {

    @TestConfiguration
    internal class DepartmentServiceTestConfig {

        @Bean
        fun departmentRepository() = mockk<DepartmentRepository>()

        @Bean
        fun roomService() = mockk<RoomService>()

        @Bean
        fun roomNormService() = mockk<RoomNormService>()
    }

    @Autowired
    private lateinit var sut: DepartmentService

    @Autowired
    private lateinit var roomService: RoomService

    @Autowired
    private lateinit var roomNormService: RoomNormService

    @Autowired
    private lateinit var departmentRepository: DepartmentRepository

    @Test
    fun getDepartments_shouldInvokeRepository() {
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )
        val page: Page<Department> = PageImpl(listOf(department), pageable, 1)
        every { departmentRepository.findAll(pageable) }
            .returns(page)
        val result = sut.getDepartments(pageable)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(PageImpl(listOf(department), pageable, 1))
    }

    @Test
    fun checkIn_shouldInvokeRepositoryAndReturnResult() {
        // given
        val roomNormId = UUID.randomUUID()
        val roomNorm = RoomNorm(
            id = roomNormId,
            peopleCount = 5L,
            size = 10L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val roomId = UUID.randomUUID()
        val room = Room(
            id = roomId,
            roomNorm = roomNorm,
            capacity = 5L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val departmentId = UUID.randomUUID()
        every {
            roomService.findByDepartmentIdAndPersonOxygenNorm(
                departmentId,
                PERSON_OXYGEN_NORM
            )
        }
            .returns(room)
        val updatedRoomNorm = RoomNorm(
            id = roomNormId,
            peopleCount = 6L,
            size = 10L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every {
            roomNormService.save(
                any()
            )
        }
            .returns(updatedRoomNorm)
        val expected = CheckInResponse(
            departmentId = departmentId,
            roomId = roomId,
            personCount = 6L
        )

        // when
        val result = sut.checkIn(departmentId)

        // then
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify {
            roomService.findByDepartmentIdAndPersonOxygenNorm(
                departmentId,
                PERSON_OXYGEN_NORM
            )
        }
        verify { roomNormService.save(roomNorm) }
    }
}