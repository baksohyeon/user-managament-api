package io.dhlottery.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Page

@Schema(
    description = "사용자 정보 응답 DTO",
    example = """
        {
            "id": 1,
            "email": "user@example.com",
            "name": "홍길동"
        }
    """
)
data class UserDto(
    @Schema(
        description = "사용자 고유 식별자",
        example = "1",
        minimum = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val id: Long = 0,
    
    @Schema(
        description = "사용자 이메일 주소 (고유값)",
        example = "user@example.com",
        format = "email",
        maxLength = 255,
        pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )
    val email: String,
    
    @Schema(
        description = "사용자 이름",
        example = "홍길동",
        minLength = 2,
        maxLength = 50
    )
    val name: String
)

@Schema(
    description = "페이지네이션된 사용자 목록 응답 DTO",
    example = """
        {
            "content": [
                {
                    "id": 1,
                    "email": "user1@example.com",
                    "name": "홍길동"
                },
                {
                    "id": 2,
                    "email": "user2@example.com",
                    "name": "김철수"
                }
            ],
            "pageNumber": 0,
            "pageSize": 20,
            "totalElements": 100,
            "totalPages": 5,
            "first": true,
            "last": false,
            "empty": false
        }
    """
)
data class PagedUserResponse(
    @Schema(
        description = "현재 페이지의 사용자 목록",
        type = "array"
    )
    val content: List<UserDto>,
    
    @Schema(
        description = "현재 페이지 번호 (0부터 시작)",
        example = "0",
        minimum = "0"
    )
    val pageNumber: Int,
    
    @Schema(
        description = "페이지 크기 (한 페이지당 항목 수)",
        example = "20",
        minimum = "1",
        maximum = "100"
    )
    val pageSize: Int,
    
    @Schema(
        description = "전체 사용자 수",
        example = "100",
        minimum = "0"
    )
    val totalElements: Long,
    
    @Schema(
        description = "전체 페이지 수",
        example = "5",
        minimum = "0"
    )
    val totalPages: Int,
    
    @Schema(
        description = "첫 번째 페이지 여부",
        example = "true"
    )
    val first: Boolean,
    
    @Schema(
        description = "마지막 페이지 여부",
        example = "false"
    )
    val last: Boolean,
    
    @Schema(
        description = "빈 페이지 여부 (데이터가 없는 경우)",
        example = "false"
    )
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

@Schema(
    description = "사용자 생성 요청 DTO",
    example = """
        {
            "email": "newuser@example.com",
            "password": "securePassword123",
            "name": "새로운사용자"
        }
    """
)
data class CreateUserRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    @Schema(
        description = "사용자 이메일 주소 (고유값, 필수)",
        example = "newuser@example.com",
        required = true,
        format = "email",
        maxLength = 255,
        pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(
        description = "사용자 비밀번호 (최소 6자, 필수)",
        example = "securePassword123",
        required = true,
        minLength = 6,
        maxLength = 100,
        format = "password"
    )
    val password: String,

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(
        description = "사용자 이름 (2-50자, 필수)",
        example = "새로운사용자",
        required = true,
        minLength = 2,
        maxLength = 50
    )
    val name: String
)

@Schema(
    description = "사용자 수정 요청 DTO",
    example = """
        {
            "email": "updated@example.com",
            "name": "수정된이름"
        }
    """
)
data class UpdateUserRequest(
    @field:Email(message = "Email should be valid")
    @Schema(
        description = "수정할 이메일 주소 (선택사항, 고유값)",
        example = "updated@example.com",
        required = false,
        format = "email",
        maxLength = 255,
        pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        nullable = true
    )
    val email: String? = null,

    @field:Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(
        description = "수정할 사용자 이름 (선택사항, 2-50자)",
        example = "수정된이름",
        required = false,
        minLength = 2,
        maxLength = 50,
        nullable = true
    )
    val name: String? = null
) 