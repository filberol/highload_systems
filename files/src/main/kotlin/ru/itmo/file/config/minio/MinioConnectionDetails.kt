package ru.itmo.file.config.minio

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails

interface MinioConnectionDetails : ConnectionDetails {
    val userUrl: String?
    val url: String?

    val accessKey: String?

    val secretKey: String?

    val bucket: String?
}