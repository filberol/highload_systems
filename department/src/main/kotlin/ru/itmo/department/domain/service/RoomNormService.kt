package ru.itmo.department.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.department.infra.model.RoomNorm
import ru.itmo.department.infra.repository.RoomNormRepository

@Service
class RoomNormService(
    private val roomNormRepository: RoomNormRepository
) {

    @Transactional(readOnly = false)
    fun save(roomNorm: RoomNorm): RoomNorm {
        return roomNormRepository.save(roomNorm)
    }
}