package ru.itmo.highload_systems.api.controller

import jakarta.validation.constraints.Positive
import org.springframework.web.bind.annotation.*
import ru.itmo.highload_systems.api.dto.OxygenSupplyResponse
import ru.itmo.highload_systems.domain.service.OxygenSupplyService
import java.util.*

@RestController
class OxygenSupply(
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
    fun getOxygenSupplies(): List<OxygenSupplyResponse> {
        return oxygenSupplyService.findAll()
    }

    @GetMapping("/oxygen-supply/{id}")
    fun getOxygenSupplyById(@PathVariable id: UUID): OxygenSupplyResponse {
        return oxygenSupplyService.findById(id)
    }
}
