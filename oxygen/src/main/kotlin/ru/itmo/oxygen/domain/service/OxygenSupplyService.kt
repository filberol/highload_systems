package ru.itmo.oxygen.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.oxygen.api.dto.OxygenSupplyResponse
import ru.itmo.oxygen.domain.mapper.OxygenSupplyApiMapper
import ru.itmo.oxygen.infra.model.OxygenSupply
import ru.itmo.oxygen.infra.repository.OxygenStorageRepository
import ru.itmo.oxygen.infra.repository.OxygenSupplyRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class OxygenSupplyService(
    private val oxygenSupplyRepository: OxygenSupplyRepository,
    private val oxygenStorageRepository: OxygenStorageRepository,
    private val oxygenSupplyApiMapper: OxygenSupplyApiMapper
) {

    @Transactional
    fun create(size: Long, toDepartmentId: UUID): OxygenSupplyResponse {
        val supply = OxygenSupply(
            size = size,
            departmentId = toDepartmentId
        )
        return oxygenSupplyApiMapper.toDto(oxygenSupplyRepository.save(supply))
    }

    @Transactional
    fun processById(id: UUID): OxygenSupplyResponse {
        val supply = findEntityById(id)
        val storage = oxygenStorageRepository.findByCapacityGreaterThan(supply.size!!)
            .orElseThrow {
                IllegalArgumentException(
                    "Нет доступного воздуха размера: %s для перевозки в департамент с id: %".format(
                        supply.departmentId,
                        supply.size
                    )
                )
            }
        storage.size = storage.size?.minus(supply.size!!)
        storage.onSaveHook()
        supply.oxygenStorage = storage
        supply.onSaveHook()
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