package ru.itmo.auth.domain.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.auth.api.dto.AuthRequest
import ru.itmo.auth.api.dto.AuthResponse
import ru.itmo.auth.api.dto.RegisterRequest
import ru.itmo.auth.domain.mapper.UserMapper
import java.util.*

@Service
class AuthService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val userMapper: UserMapper
) {

    @Transactional
    fun register(request: RegisterRequest): Optional<AuthResponse> {
        val user = userService.register(request)
        val jwt: String = jwtService.generateToken(userMapper.toDetails(user))
        return Optional.of(
            AuthResponse(
                userId = user.id,
                token = jwt
            )
        )
    }

    fun authenticate(request: AuthRequest): Optional<AuthResponse> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.login, request.password
            )
        )
        val user = userService.findByLogin(request.login)
        val jwt = jwtService.generateToken(userMapper.toDetails(user))
        return Optional.of(
            AuthResponse(
                userId = user.id,
                token = jwt
            )
        )
    }

}