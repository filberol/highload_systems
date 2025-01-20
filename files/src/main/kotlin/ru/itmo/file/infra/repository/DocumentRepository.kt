package ru.itmo.file.infra.repository

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import ru.itmo.file.infra.model.Document
import java.util.*

@Repository
interface DocumentRepository : R2dbcRepository<Document, UUID> {

    fun existsByKey(key: String): Mono<Boolean>
}