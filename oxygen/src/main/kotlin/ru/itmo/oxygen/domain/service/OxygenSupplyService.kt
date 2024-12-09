package ru.itmo.oxygen.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.oxygen.api.dto.OxygenSupplyResponse
import ru.itmo.oxygen.clients.DepartmentClient
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
    private val oxygenSupplyApiMapper: OxygenSupplyApiMapper,
    private val departmentClient: DepartmentClient
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
    fun processById(token: String, id: UUID): OxygenSupplyResponse {
        val supply = findEntityById(id)
        val storages = oxygenStorageRepository.findByCapacityGreaterThan(supply.size!!)
        if (storages.isEmpty()) {
            throw IllegalArgumentException(
                "Нет доступного воздуха размера: ${supply.size} для перевозки в департамент с ${supply.departmentId}: %"
            )
        }
        departmentClient.supplyOxygen(token, supply.departmentId!!, supply.size!!)
        val storage = storages.first()
        storage.size = storage.size?.minus(supply.size!!)
        supply.oxygenStorage = storage
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