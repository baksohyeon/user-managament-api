package io.dhlottery.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.time.LocalDateTime

@Schema(description = "에러 응답 DTO")
data class ErrorResponse(
    @Schema(description = "에러 코드", example = "USER_NOT_FOUND")
    val code: String,

    @Schema(description = "에러 메시지", example = "User not found with id: 1")
    val message: String,

    @Schema(description = "에러 발생 시간", example = "2024-01-01T12:00:00")
    val timestamp: Instant,

    @Schema(description = "요청 경로", example = "/api/users/1")
    val path: String? = null
)

@Schema(description = "유효성 검증 에러 응답 DTO")
data class ValidationErrorResponse(
    @Schema(description = "에러 코드", example = "VALIDATION_ERROR")
    val code: String = "VALIDATION_ERROR",

    @Schema(description = "전체 에러 메시지", example = "Validation failed")
    val message: String,

    @Schema(description = "에러 발생 시간", example = "2024-01-01T12:00:00")
    val timestamp: Instant? = Instant.now(),

    @Schema(description = "요청 경로", example = "/api/users")
    val path: String? = null,

    @Schema(description = "필드별 에러 상세 정보")
    val fieldErrors: List<FieldError>
)

@Schema(description = "필드 에러 정보")
data class FieldError(
    @Schema(description = "에러가 발생한 필드명", example = "email")
    val field: String,
    
    @Schema(description = "거부된 값", example = "invalid-email")
    val rejectedValue: Any?,
    
    @Schema(description = "에러 메시지", example = "Email should be valid")
    val message: String
) 