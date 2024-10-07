package ru.itmo.highload_systems.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.highload_systems.model.entity.Department

@Repository
interface DepartmentRepository: JpaRepository<Department, Int>