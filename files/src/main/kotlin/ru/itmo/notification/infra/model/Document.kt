package ru.itmo.notification.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Entity
@Table(name = "documents")
class Document(
    @Id
    @jakarta.persistence.Id @GeneratedValue @UuidGenerator
    @Column("id")
    val id: UUID? = null,
    @Column("bucket")
    val bucket: String?,
    @Column("key")
    val key: String?,
)