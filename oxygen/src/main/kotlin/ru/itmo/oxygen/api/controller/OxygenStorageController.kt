package ru.itmo.oxygen.api.controller

import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.itmo.oxygen.api.dto.OxygenStorageResponse
import ru.itmo.oxygen.domain.service.OxygenStorageService
import java.util.*

@RestController
class OxygenStorageController(
    private val oxygenStorageService: OxygenStorageService
) {

    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'ADMIN')")
    @GetMapping("/storages")
    fun getStorages(
        @PageableDefault(
            sort = ["id"],
            size = 50
        ) pageable: Pageable
    ): Page<OxygenStorageResponse> {
        return oxygenStorageService.findAll(pageable)
    }

    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'ADMIN')")
    @GetMapping("/storages/{id}")
    fun getStorageById(@PathVariable id: UUID): OxygenStorageResponse {
        return oxygenStorageService.findById(id)
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/storages")
    fun createStorage(@RequestParam @Positive size: Long): OxygenStorageResponse {
        return oxygenStorageService.create(size)
    }
}