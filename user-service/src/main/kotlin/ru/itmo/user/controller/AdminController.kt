package ru.itmo.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.itmo.user.exceptions.NotFoundException
import ru.itmo.user.exceptions.UserBlockedException
import ru.itmo.user.services.AdminService

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService
) {

    @Operation(description = "Delete user", summary = "Delete user by id if exists", tags = ["Admin"])
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Success"), ApiResponse(
            responseCode = "404",
            description = "User not found"
        ), ApiResponse(responseCode = "401", description = "Unauthorized"), ApiResponse(
            responseCode = "403",
            description = "No rights"
        )]
    )
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun deleteUser(@PathVariable userId: @NotNull @Min(1) Long): ResponseEntity<*> {
        try {
            adminService.deleteUser(userId)
            return ResponseEntity.ok("Пользователь с id $userId успешно удален")
        } catch (e: NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }

    @Operation(description = "Set ADMIN role", summary = "Set admin role to user if exists", tags = ["Admin"])
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Success"), ApiResponse(
            responseCode = "404",
            description = "User not found"
        ), ApiResponse(responseCode = "401", description = "Unauthorized"), ApiResponse(
            responseCode = "403",
            description = "No rights"
        )]
    )
    @PostMapping("/set-admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun setAdminRole(@RequestBody userId: @NotNull @Min(1) Long): ResponseEntity<*> {
        try {
            adminService.setAdminRole(userId)
            return ResponseEntity.ok("Роль успешно назначена")
        } catch (e: NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: UserBlockedException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }

    @Operation(description = "Remove ADMIN role", summary = "Remove ADMIN role if user exists", tags = ["Admin"])
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Success"), ApiResponse(
            responseCode = "404",
            description = "User not found"
        ), ApiResponse(responseCode = "401", description = "Unauthorized"), ApiResponse(
            responseCode = "403",
            description = "No rights"
        )]
    )
    @PostMapping("/remove-admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun removeAdminRole(@RequestBody userId: @NotNull @Min(1) Long): ResponseEntity<*> {
        try {
            adminService.removeAdminRole(userId)
            return ResponseEntity.ok("Роль успешно удалена")
        } catch (e: NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }


    @Operation(
        description = "Set PREMIUM USER role",
        summary = "Set PREMIUM USER role if user exists",
        tags = ["Admin"]
    )
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Success"), ApiResponse(
            responseCode = "404",
            description = "User not found"
        ), ApiResponse(responseCode = "401", description = "Unauthorized"), ApiResponse(
            responseCode = "403",
            description = "No rights"
        )]
    )
    @PostMapping("/premium")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun setPremiumUserRole(@RequestBody userId: @NotNull @Min(1) Long): ResponseEntity<*> {
        try {
            adminService.setPremiumUserRole(userId)
            return ResponseEntity.ok("Роль успешно назначена")
        } catch (e: NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: UserBlockedException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }


    @Operation(
        description = "Set BLOCKED USER role",
        summary = "Set BLOCKED USER role if user exists",
        tags = ["Admin"]
    )
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Success"), ApiResponse(
            responseCode = "404",
            description = "User not found"
        ), ApiResponse(responseCode = "401", description = "Unauthorized"), ApiResponse(
            responseCode = "403",
            description = "No rights"
        )]
    )
    @PostMapping("/blocked")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun setBlockedUserRole(@RequestBody userId: @NotNull @Min(1) Long): ResponseEntity<*> {
        try {
            adminService.setBlockedUserRole(userId)
            return ResponseEntity.ok("Роль успешно назначена")
        } catch (e: NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }

    @Operation(
        description = "Set STANDARD USER role",
        summary = "Set STANDARD USER role if user exists",
        tags = ["Admin"]
    )
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Success"), ApiResponse(
            responseCode = "404",
            description = "User not found"
        ), ApiResponse(responseCode = "401", description = "Unauthorized"), ApiResponse(
            responseCode = "403",
            description = "No rights"
        )]
    )
    @PostMapping("/standard")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun setStandardUserRole(@RequestBody userId: @NotNull @Min(1) Long): ResponseEntity<*> {
        try {
            adminService.setStandardUserRole(userId)
            return ResponseEntity.ok("Роль успешно назначена")
        } catch (e: NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }
}