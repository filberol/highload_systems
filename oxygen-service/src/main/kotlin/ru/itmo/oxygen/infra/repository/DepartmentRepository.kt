package ru.itmo.highload_systems.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.highload_systems.infra.model.Department
import java.util.UUID

@Repository
interface DepartmentRepository: JpaRepository<Department, UUID>