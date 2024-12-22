package ru.itmo.files.infra.repository

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import ru.itmo.files.infra.model.Document
import java.util.*

@Repository
interface DocumentRepository : R2dbcRepository<Document, UUID> {
}