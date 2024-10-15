package ru.itmo.highload_systems.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems.api.dto.OxygenStorageResponse
import ru.itmo.highload_systems.domain.mapper.OxygenStorageApiMapper
import ru.itmo.highload_systems.infra.repository.OxygenStorageRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class OxygenStorageService(
    private val oxygenStorageRepository: OxygenStorageRepository,
    private val oxygenStorageApiMapper: OxygenStorageApiMapper
) {

    fun findAll(pageable: Pageable): Page<OxygenStorageResponse> {
        return oxygenStorageApiMapper.toDto(oxygenStorageRepository.findAll(pageable))
    }

    fun findById(id: UUID): OxygenStorageResponse {
        val oxygenStorage = oxygenStorageRepository.findById(id).orElseThrow()
        return oxygenStorageApiMapper.toDto(oxygenStorage)
    }

    fun findByDepartmentId(departmentId: UUID): OxygenStorageResponse {
        val storage = oxygenStorageRepository.findByDepartmentId(departmentId).orElseThrow()
        return oxygenStorageApiMapper.toDto(storage)
    }
}