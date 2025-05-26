package io.dhlottery.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "사용자 정보 응답 DTO")
data class UserDto(
    @Schema(description = "사용자 ID", example = "1")
    val id: Long = 0,
    
    @Schema(description = "사용자 이메일", example = "user@example.com")
    val email: String,
    
    @Schema(description = "사용자 이름", example = "홍길동")
    val name: String
)

@Schema(description = "사용자 생성 요청 DTO")
data class CreateUserRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    @Schema(description = "사용자 이메일", example = "user@example.com", required = true)
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "사용자 비밀번호 (최소 6자)", example = "password123", required = true)
    val password: String,

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(description = "사용자 이름 (2-50자)", example = "홍길동", required = true)
    val name: String
)

@Schema(description = "사용자 수정 요청 DTO")
data class UpdateUserRequest(
    @field:Email(message = "Email should be valid")
    @Schema(description = "수정할 이메일 (선택사항)", example = "newemail@example.com", required = false)
    val email: String? = null,

    @field:Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(description = "수정할 이름 (선택사항, 2-50자)", example = "김철수", required = false)
    val name: String? = null
) 