package io.dhlottery.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Page

@Schema(description = "사용자 정보 응답 DTO")
data class UserDto(
    @Schema(description = "사용자 ID", example = "1")
    val id: Long = 0,
    
    @Schema(description = "사용자 이메일", example = "user@example.com")
    val email: String,
    
    @Schema(description = "사용자 이름", example = "홍길동")
    val name: String
)

@Schema(description = "페이지네이션된 사용자 목록 응답 DTO")
data class PagedUserResponse(
    @Schema(description = "사용자 목록")
    val content: List<UserDto>,
    
    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    val pageNumber: Int,
    
    @Schema(description = "페이지 크기", example = "20")
    val pageSize: Int,
    
    @Schema(description = "전체 요소 수", example = "100")
    val totalElements: Long,
    
    @Schema(description = "전체 페이지 수", example = "5")
    val totalPages: Int,
    
    @Schema(description = "첫 번째 페이지 여부", example = "true")
    val first: Boolean,
    
    @Schema(description = "마지막 페이지 여부", example = "false")
    val last: Boolean,
    
    @Schema(description = "빈 페이지 여부", example = "false")
    val empty: Boolean
) {
    companion object {
        fun from(page: Page<UserDto>): PagedUserResponse {
            return PagedUserResponse(
                content = page.content,
                pageNumber = page.number,
                pageSize = page.size,
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                first = page.isFirst,
                last = page.isLast,
                empty = page.isEmpty
            )
        }
    }
}

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