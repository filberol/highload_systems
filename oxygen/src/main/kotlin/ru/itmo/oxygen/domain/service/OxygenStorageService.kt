package ru.itmo.oxygen.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.oxygen.api.dto.OxygenStorageResponse
import ru.itmo.oxygen.domain.mapper.OxygenStorageApiMapper
import ru.itmo.oxygen.infra.model.OxygenStorage
import ru.itmo.oxygen.infra.repository.OxygenStorageRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class OxygenStorageService(
    private val oxygenStorageRepository: OxygenStorageRepository,
    private val oxygenStorageApiMapper: OxygenStorageApiMapper
) {

    fun findAll(pageable: Pageable): Page<OxygenStorageResponse> {
        return oxygenStorageRepository.findAll(pageable).map(oxygenStorageApiMapper::toDto)
    }

    fun findById(id: UUID): OxygenStorageResponse {
        val oxygenStorage = oxygenStorageRepository.findById(id)
            .orElseThrow { NoSuchElementException("Хранилище c id %s не найдено".format(id)) }
        return oxygenStorageApiMapper.toDto(oxygenStorage)
    }

    @Transactional
    fun create(size: Long): OxygenStorageResponse {
        val storage = OxygenStorage(size = size)
        val saved = oxygenStorageRepository.save(storage)
        return oxygenStorageApiMapper.toDto(saved)
    }
}