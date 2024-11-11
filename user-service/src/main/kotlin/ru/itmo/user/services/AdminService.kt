package ru.itmo.user.services

import org.springframework.stereotype.Service
import ru.itmo.user.clients.InventoryClient
import ru.itmo.user.exceptions.NotFoundException
import ru.itmo.user.exceptions.UserBlockedException
import ru.itmo.user.model.entity.UserEntity

@Service
class AdminService(
    private val userService: UserService,
    private val roleService: RoleService,
    private val inventoryClient: InventoryClient
) {
    
    fun deleteUser(id: Long) {
        if (!userService.existsById(id)) throw NotFoundException("Пользователь с id $id не найден")
        inventoryClient.deleteAll(id)
        userService.deleteById(id)
    }

    fun setAdminRole(id: Long) {
        val user: UserEntity = userService.findById(id)
            .orElseThrow { NotFoundException("Пользователь с id $id не найден") }
        if (user.roles!!
                .contains(roleService.blockedUserRole)
        ) throw UserBlockedException("Пользователь не может быть админом, так как он в черном списке")
        if (!user.roles.contains(roleService.adminRole)) {
            user.addRole(roleService.adminRole)
            userService.save(user)
        }
    }

    fun removeAdminRole(id: Long) {
        val user: UserEntity = userService.findById(id)
            .orElseThrow { NotFoundException("Пользователь с id $id не найден") }
        user.removeRole(roleService.adminRole)
        userService.save(user)
    }

    fun setPremiumUserRole(id: Long) {
        val user: UserEntity = userService.findById(id)
            .orElseThrow { NotFoundException("Пользователь с id $id не найден") }
        if (user.roles!!
                .contains(roleService.blockedUserRole)
        ) throw UserBlockedException("Пользователь не может быть премиумом, так как он в черном списке")
        if (!user.roles.contains(roleService.premiumUserRole)) {
            user.removeRole(roleService.standardUserRole)
            user.addRole(roleService.premiumUserRole)
            userService.save(user)
        }
    }

    fun setBlockedUserRole(id: Long) {
        val user: UserEntity = userService!!.findById(id)
            .orElseThrow { NotFoundException("Пользователь с id $id не найден") }
        user.clearRoles()
        user.addRole(roleService.blockedUserRole)
        userService.save(user)
    }

    @Throws(NotFoundException::class)
    fun setStandardUserRole(id: Long) {
        val user: UserEntity = userService.findById(id)
            .orElseThrow { NotFoundException("Пользователь с id $id не найден") }
        if (user.roles!!.contains(roleService.blockedUserRole)) {
            user.removeRole(roleService.blockedUserRole)
            user.addRole(roleService.standardUserRole)
        }
        if (user.roles.contains(roleService.premiumUserRole)) {
            user.removeRole(roleService.premiumUserRole)
            user.addRole(roleService.premiumUserRole)
        }
        userService.save(user)
    }
}