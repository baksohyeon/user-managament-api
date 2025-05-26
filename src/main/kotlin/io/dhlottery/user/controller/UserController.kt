package io.dhlottery.user.controller

import io.dhlottery.user.dto.CreateUserRequest
import io.dhlottery.user.dto.ErrorResponse
import io.dhlottery.user.dto.PagedUserResponse
import io.dhlottery.user.dto.UpdateUserRequest
import io.dhlottery.user.dto.UserDto
import io.dhlottery.user.dto.ValidationErrorResponse
import io.dhlottery.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "사용자 관리 API")
class UserController(
    private val userService: UserService
) {
    @Operation(
        summary = "전체 사용자 조회",
        description = "페이지네이션을 지원하는 전체 사용자 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "사용자 목록 조회 성공",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = PagedUserResponse::class))]
            )
        ]
    )
    @GetMapping
    fun getAllUsers(
        @Parameter(description = "페이지네이션 정보 (기본값: page=0, size=20)")
        @PageableDefault(size = 20, sort = ["id"]) pageable: Pageable
    ): ResponseEntity<PagedUserResponse> {
        val users = userService.getAllUsers(pageable)
        return ResponseEntity.ok(users)
    }

    @Operation(
        summary = "ID로 사용자 조회",
        description = "사용자 ID를 통해 특정 사용자 정보를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "사용자 조회 성공",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없음",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getUserById(
        @Parameter(description = "사용자 ID", required = true, example = "1")
        @PathVariable id: Long
    ): ResponseEntity<UserDto> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @Operation(
        summary = "이메일로 사용자 조회",
        description = "이메일 주소를 통해 특정 사용자 정보를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "사용자 조회 성공",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없음",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/email/{email}")
    fun getUserByEmail(
        @Parameter(description = "사용자 이메일", required = true, example = "user@example.com")
        @PathVariable email: String
    ): ResponseEntity<UserDto> {
        val user = userService.getUserByEmail(email)
        return ResponseEntity.ok(user)
    }

    @Operation(
        summary = "새 사용자 생성",
        description = "새로운 사용자를 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "사용자 생성 성공",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDto::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 데이터",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ValidationErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "이미 존재하는 이메일",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping
    fun createUser(
        @Parameter(description = "사용자 생성 요청 데이터", required = true)
        @Valid @RequestBody request: CreateUserRequest
    ): ResponseEntity<UserDto> {
        val createdUser = userService.createUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @Operation(
        summary = "사용자 정보 수정",
        description = "기존 사용자의 정보를 수정합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "사용자 수정 성공",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDto::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 데이터",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ValidationErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없음",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "이미 존재하는 이메일",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PutMapping("/{id}")
    fun updateUser(
        @Parameter(description = "사용자 ID", required = true, example = "1")
        @PathVariable id: Long,
        @Parameter(description = "사용자 수정 요청 데이터", required = true)
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserDto> {
        val updatedUser = userService.updateUser(id, request)
        return ResponseEntity.ok(updatedUser)
    }

    @Operation(
        summary = "사용자 삭제",
        description = "사용자를 삭제합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "사용자 삭제 성공",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없음",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteUser(
        @Parameter(description = "사용자 ID", required = true, example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
} 