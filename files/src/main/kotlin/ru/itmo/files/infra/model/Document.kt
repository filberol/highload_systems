package ru.itmo.files.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "files")
class Document(
    @Id
    @UuidGenerator
    val id: UUID? = null,

    val bucket: String?,

    val key: String?,
) {}