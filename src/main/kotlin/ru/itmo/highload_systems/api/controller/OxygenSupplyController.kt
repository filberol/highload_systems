package ru.itmo.highload_systems.api.controller

import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import ru.itmo.highload_systems.api.dto.OxygenSupplyResponse
import ru.itmo.highload_systems.domain.service.OxygenSupplyService
import java.util.*

@RestController
class OxygenSupplyController(
    private val oxygenSupplyService: OxygenSupplyService
) {

    @PostMapping("/oxygen-supply")
    fun create(
        @RequestParam @Positive size: Long,
        @RequestParam departmentId: UUID
    ): OxygenSupplyResponse {
        return oxygenSupplyService.create(size, departmentId)
    }

    @PostMapping("/oxygen-supply/{id}")
    fun process(@PathVariable id: UUID): OxygenSupplyResponse {
        return oxygenSupplyService.processById(id)
    }

    @GetMapping("/oxygen-supply")
    fun getOxygenSupplies(pageable: Pageable): Page<OxygenSupplyResponse> {
        return oxygenSupplyService.findAll(pageable)
    }

    @GetMapping("/oxygen-supply/{id}")
    fun getOxygenSupplyById(@PathVariable id: UUID): OxygenSupplyResponse {
        return oxygenSupplyService.findById(id)
    }
}
