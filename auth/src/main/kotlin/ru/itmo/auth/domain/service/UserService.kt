package ru.itmo.auth.domain.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.auth.api.dto.CreateUserRequest
import ru.itmo.auth.api.dto.RegisterRequest
import ru.itmo.auth.api.dto.UpdateUserRequest
import ru.itmo.auth.api.dto.UserResponse
import ru.itmo.auth.domain.mapper.RoleMapper
import ru.itmo.auth.domain.mapper.UserMapper
import ru.itmo.auth.infra.model.User
import ru.itmo.auth.infra.repository.UserRepository
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleMapper: RoleMapper,
    private val userMapper: UserMapper
) : UserDetailsService {

    @Transactional
    fun create(request: CreateUserRequest): UserResponse {
        findOptionalByLogin(request.login)
            .ifPresent {
                throw IllegalArgumentException(
                    "Пользователь с логином %s уже существует".format(
                        request.login
                    )
                )
            }
        val user = userMapper.toEntity(request)
        return userMapper.toResponse(userRepository.save(user))
    }

    @Transactional
    fun register(request: RegisterRequest): UserResponse {
        findOptionalByLogin(request.login)
            .ifPresent {
                throw NoSuchElementException(
                    "Пользователь с логином %s уже существует".format(
                        request.login
                    )
                )
            }
        val user = userMapper.toEntity(request)
        return userMapper.toResponse(userRepository.save(user))
    }

    @Transactional
    fun update(request: UpdateUserRequest): UserResponse {
        val user = findOptionalByLogin(request.login)
            .orElseThrow {
                throw NoSuchElementException(
                    "Пользователь с логином %s не найден".format(
                        request.login
                    )
                )
            }
        user.role = roleMapper.toEntity(request.role)
        return userMapper.toResponse(userRepository.save(user))
    }

    fun findByLogin(login: String): UserResponse {
        val user = findOptionalByLogin(login)
            .orElseThrow {
                throw NoSuchElementException(
                    "Пользователь с логином %s не найден".format(
                        login
                    )
                )
            }
        return userMapper.toResponse(user)
    }

    @Transactional(readOnly = true)
    private fun findOptionalByLogin(login: String): Optional<User> {
        return userRepository.findByLogin(login)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return userMapper.toDetails(findByLogin(username))
    }
}