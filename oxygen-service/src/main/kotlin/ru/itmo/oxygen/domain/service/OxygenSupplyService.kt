package ru.itmo.highload_systems.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems.api.dto.OxygenSupplyResponse
import ru.itmo.highload_systems.domain.mapper.OxygenSupplyApiMapper
import ru.itmo.highload_systems.infra.model.OxygenSupply
import ru.itmo.highload_systems.infra.repository.OxygenStorageRepository
import ru.itmo.highload_systems.infra.repository.OxygenSupplyRepository
import java.time.OffsetDateTime
import java.util.*

@Service
@Transactional(readOnly = true)
class OxygenSupplyService(
    private val oxygenSupplyRepository: OxygenSupplyRepository,
    private val oxygenSupplyApiMapper: OxygenSupplyApiMapper,
    private val departmentService: DepartmentService,
    private val oxygenStorageRepository: OxygenStorageRepository
) {

    @Transactional(readOnly = false)
    fun create(size: Long, toDepartmentId: UUID): OxygenSupplyResponse {
        val department = departmentService.findById(toDepartmentId)
        val supply = OxygenSupply(
            size = size,
            department = department,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        return oxygenSupplyApiMapper.toDto(oxygenSupplyRepository.save(supply))
    }


    @Transactional(readOnly = false)
    fun processById(id: UUID): OxygenSupplyResponse {
        val supply = findEntityById(id)
        val storage = oxygenStorageRepository.findByDepartmentIdAndCapacityGreaterThan(
            supply.department.id!!,
            supply.size
        ).orElseThrow {
            IllegalArgumentException(
                "В департаменте с id %s нет свободного места %s".format(
                    supply.department.id,
                    supply.size
                )
            )
        }
        storage.size += supply.size
        storage.updatedAt = OffsetDateTime.now()
        supply.oxygenStorage = storage
        supply.updatedAt = OffsetDateTime.now()
        oxygenStorageRepository.save(storage)
        return oxygenSupplyApiMapper.toDto(oxygenSupplyRepository.save(supply))
    }

    fun findById(id: UUID): OxygenSupplyResponse {
        return oxygenSupplyApiMapper.toDto(findEntityById(id))
    }

    private fun findEntityById(id: UUID): OxygenSupply {
        return oxygenSupplyRepository.findById(id)
            .orElseThrow {
                NoSuchElementException(
                    "Заявка на снабжение кислородом с id %s не найдена".format(
                        id
                    )
                )
            }
    }

    fun findAll(pageable: Pageable): Page<OxygenSupplyResponse> {
        return oxygenSupplyRepository.findAll(pageable).map(oxygenSupplyApiMapper::toDto)
    }

}