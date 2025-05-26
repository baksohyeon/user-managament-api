package io.dhlottery.user.service

import io.dhlottery.user.dto.CreateUserRequest
import io.dhlottery.user.dto.PagedUserResponse
import io.dhlottery.user.dto.UpdateUserRequest
import io.dhlottery.user.dto.UserDto
import io.dhlottery.user.entity.User
import io.dhlottery.user.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {

    fun getAllUsers(pageable: Pageable): PagedUserResponse {
        val validatedPageable = validateAndSanitizePageable(pageable)
        val page = userRepository.findAll(validatedPageable).map { it.toDto() }
        return PagedUserResponse.from(page)
    }

    private fun validateAndSanitizePageable(pageable: Pageable): Pageable {
        val validProperties = setOf("id", "email", "name", "password")
        
        // Check if sort properties are valid
        val sortProperties = pageable.sort.toList().map { it.property }
        val invalidProperties = sortProperties.filter { !validProperties.contains(it) }
        
        if (invalidProperties.isNotEmpty()) {
            // If invalid properties found, use default sort by id
            return PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                Sort.by("id")
            )
        }
        
        return pageable
    }

    fun getUserById(id: Long): UserDto {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User not found with id: $id") }
        return user.toDto()
    }

    fun getUserByEmail(email: String): UserDto {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UserNotFoundException("User not found with email: $email") }
        return user.toDto()
    }

    fun createUser(request: CreateUserRequest): UserDto {
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException("User already exists with email: ${request.email}")
        }

        val user = User(
            email = request.email,
            password = request.password, // In production, this should be hashed
            name = request.name
        )

        val savedUser = userRepository.save(user)
        return savedUser.toDto()
    }

    fun updateUser(id: Long, request: UpdateUserRequest): UserDto {
        val existingUser = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User not found with id: $id") }

        // Check if email is being updated and if it already exists
        request.email?.let { newEmail ->
            if (newEmail != existingUser.email && userRepository.existsByEmail(newEmail)) {
                throw UserAlreadyExistsException("User already exists with email: $newEmail")
            }
        }

        val updatedUser = existingUser.copy(
            email = request.email ?: existingUser.email,
            name = request.name ?: existingUser.name
        )

        val savedUser = userRepository.save(updatedUser)
        return savedUser.toDto()
    }

    fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException("User not found with id: $id")
        }
        userRepository.deleteById(id)
    }

    private fun User.toDto(): UserDto = UserDto(
        id = this.id,
        email = this.email,
        name = this.name
    )
}

class UserNotFoundException(message: String) : RuntimeException(message)
class UserAlreadyExistsException(message: String) : RuntimeException(message) 