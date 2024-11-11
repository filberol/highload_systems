package ru.itmo.user.controller

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.itmo.user.exceptions.NotEnoughOxygenException
import ru.itmo.user.exceptions.NotFoundException
import ru.itmo.user.model.dto.ResponseDto
import ru.itmo.user.model.dto.UserDto
import ru.itmo.user.model.dto.UserUpdateBalanceDto
import ru.itmo.user.services.UserService
import java.security.Principal

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    
    @Operation(description = "Replenish the balance", summary = "Replenish user's balance", tags = ["User"])
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Success"), ApiResponse(
            responseCode = "401",
            description = "Unauthorized"
        ), ApiResponse(responseCode = "403", description = "User not found")]
    )
    @PostMapping("/replenish-balance")
    fun replenishBalance(principal: Principal, @RequestBody sum: @NotNull @Min(1) Int): ResponseEntity<*> {
        try {
            userService.replenishBalance(principal.name, sum)
            return ResponseEntity.ok("Баланас успешно пополнен")
        } catch (e: NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }

    @Operation(description = "Get all users", summary = "Get all user", tags = ["User"])
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    fun getAllUsers(
        @PageableDefault(sort = ["id"], size = 50) pageable: Pageable
    ): List<UserDto> {
        return userService.getAll(pageable)
    }

    @Operation(description = "Buy premium account", summary = "Buy premium account", tags = ["User"])
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Success"), ApiResponse(
            responseCode = "401",
            description = "Unauthorized"
        ), ApiResponse(responseCode = "403", description = "User not found"), ApiResponse(
            responseCode = "400",
            description = "User doesn't have enough money"
        )]
    )
    @PostMapping("/buy-premium")
    fun buyPremiumAccount(principal: Principal): ResponseEntity<*> {
        try {
            userService.buyPremiumAccount(principal.name)
            return ResponseEntity.ok("Покупка успешно совершена")
        } catch (e: NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (ex: NotEnoughOxygenException) {
            return ResponseEntity.badRequest().body(ex.message)
        }
    }

    @Hidden
    @GetMapping("/get-user-by-id")
    fun getById(@RequestParam id: @NotNull @Min(1) Long): ResponseDto<UserDto?> {
        val user: UserDto = userService.getById(id)
            ?: return ResponseDto(null, NotFoundException(""), HttpStatus.NOT_FOUND)
        return ResponseDto(user, null, HttpStatus.OK)
    }

    @Hidden
    @GetMapping("/get-user-by-username")
    fun findByUsername(@RequestParam username: @NotNull String): ResponseDto<UserDto?> {
        val user: UserDto = userService.getByUsername(username)
            ?: return ResponseDto(null, NotFoundException(""), HttpStatus.NOT_FOUND)
        return ResponseDto(user, null, HttpStatus.OK)
    }

    @Hidden
    @PostMapping("/update-balance")
    fun updateBalance(@RequestBody userUpdateBalanceDto: @Valid UserUpdateBalanceDto): ResponseEntity<String> {
        try {
            userService.updateBalance(userUpdateBalanceDto.userName, userUpdateBalanceDto.newBalance)
            return ResponseEntity.ok("")
        } catch (e: NotFoundException) {
            return ResponseEntity.badRequest().body("")
        }
    }
}