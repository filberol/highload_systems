package ru.itmo.highload_systems.domain.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import ru.itmo.highload_systems.common.config.DomainMapperTestConfiguration
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.model.Room
import ru.itmo.highload_systems.infra.model.RoomNorm
import ru.itmo.highload_systems.infra.repository.RoomNormRepository
import ru.itmo.highload_systems.infra.repository.RoomRepository
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [RoomNormService::class])
@Import(value = [DomainMapperTestConfiguration::class, RoomNormServiceTest.RoomNormServiceTestConfig::class])
class RoomNormServiceTest {

    @TestConfiguration
    internal class RoomNormServiceTestConfig {
        @Bean
        fun roomNormRepository() = mockk<RoomNormRepository>()

        @Bean
        fun roomRepository() = mockk<RoomRepository>()
    }

    @Autowired
    private lateinit var sut: RoomNormService

    @Autowired
    private lateinit var roomNormRepository: RoomNormRepository

    @Autowired
    private lateinit var roomRepository: RoomRepository

    @Test
    fun fillIfExistAndReturnRoom_shouldInvokeService() {
        val id = UUID.randomUUID()
        val roomNorm = RoomNorm(
            id = UUID.randomUUID(),
            size = 10L,
            avgPersonNorm = 53.1F,
            createdAt = OffsetDateTime.now()
        )
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val room = Room(
            id = id,
            number = 1L,
            capacity = 3L,
            orders = emptyList(),
            roomNorm = roomNorm,
            department = department,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomRepository.findFirstRoomByDepartmentAndSize(id, 3L) }
            .returns(Optional.of(room))
        roomNorm.size = 3L
        every { roomNormRepository.save(roomNorm) }.returns(roomNorm)

        val result = sut.fillIfExistAndReturnRoom(id, 3L)
        assertThat(result).isPresent
        assertThat(result.get())
            .usingRecursiveComparison()
            .isEqualTo(room)
        verify { roomRepository.findFirstRoomByDepartmentAndSize(id, 3L) }
        verify { roomNormRepository.save(roomNorm) }
    }
}