package ru.itmo.file.config.minio

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("minio")
data class MinioProperties(
    val url: String,
    val endpoint: String,
    val accessKey: String,
    val secretKey: String,
    val bucket: String
)