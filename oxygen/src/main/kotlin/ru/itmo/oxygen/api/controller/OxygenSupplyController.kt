package ru.itmo.oxygen.api.controller

import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.itmo.oxygen.api.dto.OxygenSupplyResponse
import ru.itmo.oxygen.domain.service.OxygenSupplyService
import java.util.*

@RestController
class OxygenSupplyController(
    private val oxygenSupplyService: OxygenSupplyService
) {

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PostMapping("/oxygen-supply")
    fun create(
        @RequestParam @Positive size: Long,
        @RequestParam departmentId: UUID
    ): OxygenSupplyResponse {
        return oxygenSupplyService.create(size, departmentId)
    }

    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'ADMIN')")
    @PostMapping("/oxygen-supply/{id}")
    fun process(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: UUID
    ): OxygenSupplyResponse {
        return oxygenSupplyService.processById(token, id)
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'SUPPLIER', 'ADMIN')")
    @GetMapping("/oxygen-supply")
    fun getOxygenSupplies(
        @PageableDefault(sort = ["id"], size = 50) pageable: Pageable
    ): Page<OxygenSupplyResponse> {
        return oxygenSupplyService.findAll(pageable)
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'SUPPLIER', 'ADMIN')")
    @GetMapping("/oxygen-supply/{id}")
    fun getOxygenSupplyById(@PathVariable id: UUID): OxygenSupplyResponse {
        return oxygenSupplyService.findById(id)
    }
}
