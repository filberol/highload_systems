package ru.itmo.user.services

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import ru.itmo.user.model.dto.JwtResponseDto
import ru.itmo.user.model.dto.UserDto
import ru.itmo.user.model.dto.UserRegisterRequestDto
import ru.itmo.user.security.JwtTokenUtils

@Service
class AuthService(
    private val userService: UserService,
    private val jwtTokenUtils: JwtTokenUtils,
    private val authenticationManager: AuthenticationManager
) {

    fun login(username: String?, password: String?): JwtResponseDto {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        val userDetails = userService.loadUserByUsername(username!!)
        val token: String = jwtTokenUtils.generateToken(userDetails)
        return JwtResponseDto(token)
    }

    fun register(userRegisterRequest: UserRegisterRequestDto): UserDto {
        return userService.register(userRegisterRequest)
    }
}