package ru.itmo.department.infra.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import ru.itmo.department.infra.model.DepartmentUser
import java.util.*

@Repository
interface DepartmentUserRepository : ReactiveCrudRepository<DepartmentUser, UUID> {
}