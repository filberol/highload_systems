package ru.itmo.department.api.dto

import java.util.UUID

data class CheckInResponse(
    var departmentId: UUID,
    var roomId: UUID,
    var personCount: Long
)
