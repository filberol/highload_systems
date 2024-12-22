package ru.itmo.notification.api.controller.dto

import java.util.UUID

data class FileResponse(
    var id: UUID,
    var url: String
)
