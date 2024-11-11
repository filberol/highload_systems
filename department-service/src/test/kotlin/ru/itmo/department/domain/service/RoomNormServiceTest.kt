package ru.itmo.department.domain.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import ru.itmo.department.common.config.DomainMapperTestConfiguration
import ru.itmo.department.infra.model.RoomNorm
import ru.itmo.department.infra.repository.RoomNormRepository
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [RoomNormService::class])
@Import(value = [DomainMapperTestConfiguration::class, RoomNormServiceTest.RoomNormServiceTestConfig::class])
class RoomNormServiceTest {

    @TestConfiguration
    internal class RoomNormServiceTestConfig {
        @Bean
        fun roomNormRepository() = mockk<RoomNormRepository>()
    }

    @Autowired
    private lateinit var sut: RoomNormService

    @Autowired
    private lateinit var roomNormRepository: RoomNormRepository


    @Test
    fun update_shouldInvokeRepository() {
        val roomNormId = UUID.randomUUID()
        val roomNorm = RoomNorm(
            id = roomNormId,
            size = 6L,
            peopleCount = 5L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomNormRepository.save(roomNorm) }
            .returns(roomNorm)
        sut.save(roomNorm)
        verify { roomNormRepository.save(roomNorm) }
    }
}