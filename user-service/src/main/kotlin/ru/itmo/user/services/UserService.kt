package ru.itmo.user.services

import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.itmo.user.exceptions.*
import ru.itmo.user.model.dto.UserDto
import ru.itmo.user.model.dto.UserRegisterRequestDto
import ru.itmo.user.model.entity.RoleEntity
import ru.itmo.user.model.entity.UserEntity
import ru.itmo.user.repositories.UserRepository
import java.util.*
import java.util.stream.Collectors

@Service
class UserService(
    private var userRepository: UserRepository,
    private var roleService: RoleService,
    private var passwordEncoder: PasswordEncoder
) : UserDetailsService {

    fun findByUsername(username: String): Optional<UserEntity> {
        return userRepository.findByUsername(username)
    } 

    fun findById(id: Long): Optional<UserEntity> {
        return userRepository.findById(id)
    }

    fun getById(id: Long): UserDto? {
        val user: UserEntity = userRepository.getUserEntityById(id)
        return userEntityToDto(user)
    }

    fun getByUsername(username: String): UserDto? {
        val user: UserEntity = userRepository.getUserEntityByUsername(username)
        return userEntityToDto(user)
    }

    fun save(user: UserEntity) {
        userRepository.save(user)
    }

    fun existsById(id: Long): Boolean {
        return userRepository.existsById(id)
    }

    fun deleteById(id: Long) {
        userRepository.deleteById(id)
    }

    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    fun updateBalance(username: String, newBalance: Int?) {
        val user: UserEntity = findByUsername(username).orElseThrow {
            NotFoundException(
                "Пользователь с ником $username не найден"
            )
        }
        user.balance = newBalance
        userRepository.save(user)
    }

    fun replenishBalance(username: String, sum: Int) {
        val user: UserEntity = findByUsername(username)
            .orElseThrow {
                NotFoundException(
                    "Пользователь с именем $username не найден"
                )
            }
        user.balance = user.balance!! + sum
        userRepository.save(user)
    }

    fun getAll(pageable: Pageable): List<UserDto> {
        return userRepository.findAll(pageable).stream().map { user: UserEntity ->
            this.userEntityToDto(
                user
            )
        }.toList()
    }

    fun buyPremiumAccount(username: String) {
        val user: UserEntity = findByUsername(username)
            .orElseThrow {
                NotFoundException(
                    "Пользователь с именем $username не найден"
                )
            }
        if (user.balance!! < 1000) throw NotEnoughOxygenException()
        user.balance = user.balance!! - 1000
        user.removeRole(roleService.standardUserRole)
        user.addRole(roleService.premiumUserRole)
        userRepository.save(user)
    }

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: UserEntity = findByUsername(username).orElseThrow {
            UsernameNotFoundException(
                String.format("Пользователь с именем '%s' не найден", username)
            )
        }
        return User(
            user.username,
            user.password,
            user.roles!!.stream().map { role -> SimpleGrantedAuthority(role.role!!.name) }
                .collect(Collectors.toList())
        )
    }

    fun register(userRegisterRequest: @Valid UserRegisterRequestDto): UserDto {
        if (existsByUsername(userRegisterRequest.username)) {
            throw UserAlreadyExistsException(
                java.lang.String.format(
                    "Пользователь с именем '%s' уже существует",
                    userRegisterRequest.username
                )
            )
        }
        val user = UserEntity(
            username = userRegisterRequest.username,
            password = passwordEncoder.encode(userRegisterRequest.password),
            email = userRegisterRequest.email,
            balance = 0,
            roles = listOf(roleService.standardUserRole) as MutableCollection<RoleEntity>
        )
        userRepository.save(user)
        return userEntityToDto(user)
    }

    private fun userEntityToDto(user: UserEntity): UserDto {
        return UserDto(
            id = user.id!!,
            username = user.username!!,
            email = user.email!!,
            balance = user.balance!!,
            description = user.description!!,
            roles = user.roles!!.stream().map { role -> role.role!!.name }.toList() as Collection<String>
        )
    }
}