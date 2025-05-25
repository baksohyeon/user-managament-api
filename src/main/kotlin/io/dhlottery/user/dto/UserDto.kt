package io.dhlottery.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserDto(
    val id: Long = 0,
    val email: String,
    val name: String
)

data class CreateUserRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String,

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    val name: String
)

data class UpdateUserRequest(
    @field:Email(message = "Email should be valid")
    val email: String? = null,

    @field:Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    val name: String? = null
) 