package ru.itmo.auth.api.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.itmo.auth.AuthServiceApplication
import ru.itmo.auth.api.dto.*
import ru.itmo.auth.common.AbstractDatabaseTest
import ru.itmo.auth.infra.repository.UserRepository
import ru.itmo.auth.security.WithMockUser
import java.nio.charset.StandardCharsets
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(classes = [AuthServiceApplication::class])
@Sql(value = ["classpath:db/users.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserControllerTest : AbstractDatabaseTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    @WithAnonymousUser
    fun register_shouldInvokeService() {
        val request = RegisterRequest(
            name = "Added user",
            login = "added-user@yandex.ru",
            password = "password"
        )

        // when
        mockMvc.perform(
            post("/auth/register")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    @WithMockUser(login = "admin@yandex.ru")
    fun create_shouldInvokeService() {
        val request = CreateUserRequest(
            name = "Added user",
            login = "added-user@yandex.ru",
            password = "password",
            role = RoleRequestResponse.MANAGER
        )
        val before = userRepository.findAll()

        // when
        mockMvc.perform(
            post("/admin")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        // then
    }

    @Test
    @WithMockUser(login = "supplier@yandex.ru")
    fun create_shouldThrowException() {
        val request = CreateUserRequest(
            name = "Added user",
            login = "added-user@yandex.ru",
            password = "password",
            role = RoleRequestResponse.MANAGER
        )
        val before = userRepository.findAll()

        // when
        mockMvc.perform(
            post("/admin")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden)

        val after = userRepository.findAll()

        // then
        assertThat(before.size).isEqualTo(after.size)
    }

    @Test
    @WithMockUser(login = "admin@yandex.ru")
    fun update_shouldInvokeService() {
        val request = UpdateUserRequest(
            login = "manager@yandex.ru",
            role = RoleRequestResponse.SUPPLIER
        )
        val before = userRepository.findAll()

        // when
        mockMvc.perform(
            put("/admin")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    @WithMockUser(login = "admin@yandex.ru")
    fun update_shouldThrowException_whenUserNotFound() {
        val request = UpdateUserRequest(
            login = "user1245@yandex.ru",
            role = RoleRequestResponse.SUPPLIER
        )

        // when
        mockMvc.perform(
            put("/admin")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(login = "manager@yandex.ru")
    fun update_shouldThrowException() {
        val request = UpdateUserRequest(
            login = "manager@yandex.ru",
            role = RoleRequestResponse.SUPPLIER
        )
        val before = userRepository.findAll()

        // when
        mockMvc.perform(
            put("/admin")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden)
        val after = userRepository.findAll()

        // then
        assertThat(before.size).isEqualTo(after.size)
    }

    @Test
    @WithMockUser(login = "admin@yandex.ru")
    fun findByLogin_shouldReturnInfo() {
        // when
        val content = mockMvc.perform(
            get("/users/manager@yandex.ru")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, UserResponse::class.java)

        val expected = UserResponse(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de8"),
            name = "Manager",
            login = "manager@yandex.ru",
            password = "\$2a\$10\$HYXOcVWzKL0XuW24c4bN1elBGxqDSeSq1UDTRtBIzIiXYli0nM2Ze",
            role = RoleRequestResponse.MANAGER
        )
        // then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    @WithMockUser(login = "admin@yandex.ru")
    fun findByLogin_shouldThrowException_whenUserNotExist() {
        // when
        mockMvc.perform(
            get("/users/user1234@yandex.ru")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(login = "manager@yandex.ru")
    fun findByLogin_shouldThrowException() {
        // when
        mockMvc.perform(
            get("/users/manager@yandex.ru")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden)
    }

}