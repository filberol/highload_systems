package ru.itmo.auth.domain.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.auth.api.dto.AuthRequest
import ru.itmo.auth.api.dto.AuthResponse
import ru.itmo.auth.api.dto.RegisterRequest
import ru.itmo.auth.domain.mapper.UserMapper
import ru.itmo.auth.infra.repository.UserRepository

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val userMapper: UserMapper
) {

    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        userRepository.findByLogin(request.login)
            .ifPresent {
                throw IllegalArgumentException(
                    "Пользователь с логином %s уже существует".format(
                        request.login
                    )
                )
            }
        val user = userRepository.save(userMapper.toEntity(request))
        val jwt: String = jwtService.generateToken(user)
        return AuthResponse(
            userId = user.id!!,
            token = jwt
        )
    }

    @Transactional
    fun authenticate(request: AuthRequest): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.login, request.password
            )
        )
        val user = userRepository.findByLogin(request.login).orElseThrow()
        val jwt = jwtService.generateToken(user)
        return AuthResponse(
            userId = user.id!!,
            token = jwt
        )
    }

}