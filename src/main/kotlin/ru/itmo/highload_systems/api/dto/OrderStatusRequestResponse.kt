package ru.itmo.highload_systems.api.dto

enum class OrderStatusRequestResponse {
    NEW,
    OXYGEN_WAITING,
    CANCEL,
    DEATH,
    DONE
}