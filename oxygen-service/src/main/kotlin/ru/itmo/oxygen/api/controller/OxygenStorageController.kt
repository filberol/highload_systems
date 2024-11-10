package ru.itmo.highload_systems.api.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.itmo.highload_systems.api.dto.OxygenStorageResponse
import ru.itmo.highload_systems.domain.service.OxygenStorageService
import java.util.*

@RestController
class OxygenStorageController(
    private val oxygenStorageService: OxygenStorageService
) {
    @GetMapping("/storages")
    fun getStorages(
        @PageableDefault(
            sort = ["id"],
            size = 50
        ) pageable: Pageable
    ): Page<OxygenStorageResponse> {
        return oxygenStorageService.findAll(pageable)
    }

    @GetMapping("/storages/{id}")
    fun getStorageById(@PathVariable id: UUID): OxygenStorageResponse {
        return oxygenStorageService.findById(id)
    }
}