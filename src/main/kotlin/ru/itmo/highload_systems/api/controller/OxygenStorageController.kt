package ru.itmo.highload_systems.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OxygenStorageController {

    @GetMapping("/storages")
    fun getStorages(){

    }

    @GetMapping("/storages/{id}")
    fun getStorageById(){

    }
}