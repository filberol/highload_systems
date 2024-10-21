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
            department = department
        )
        return oxygenSupplyApiMapper.toDto(oxygenSupplyRepository.save(supply))
    }


    @Transactional(readOnly = false)
    fun processById(id: UUID): OxygenSupplyResponse {
        val supply = oxygenSupplyRepository.findById(id).orElseThrow()
        val storage = oxygenStorageRepository.findByDepartmentIdAndCapacityGreaterThan(
            supply.department.id!!,
            supply.size
        ).orElseThrow()
        storage.size += supply.size
        supply.oxygenStorage = storage
        oxygenStorageRepository.save(storage)
        return oxygenSupplyApiMapper.toDto(oxygenSupplyRepository.save(supply))
    }

    fun findById(id: UUID): OxygenSupplyResponse {
        return oxygenSupplyApiMapper.toDto(oxygenSupplyRepository.findById(id).orElseThrow())
    }

    fun findAll(pageable: Pageable): Page<OxygenSupplyResponse> {
        return oxygenSupplyRepository.findAll(pageable).map(oxygenSupplyApiMapper::toDto)
    }

}