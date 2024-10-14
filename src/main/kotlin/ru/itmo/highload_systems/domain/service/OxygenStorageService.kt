package ru.itmo.highload_systems.domain.service

import org.springframework.stereotype.Service
import ru.itmo.highload_systems.infra.repository.OxygenStorageRepository

@Service
class OxygenStorageService(
    private val oxygenStorageRepository: OxygenStorageRepository
) {
}