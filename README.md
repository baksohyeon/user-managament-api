# User Management API

A RESTful API for managing users built with Spring Boot and Kotlin.
http://localhost:8080/swagger-ui/index.html

## Features

- **CRUD Operations**: Create, Read, Update, Delete users
- **Pagination**: Support for paginated user listing
- **Validation**: Input validation with proper error messages
- **Exception Handling**: Global exception handling with meaningful error responses
- **H2 Database**: In-memory database for development and testing

## API Endpoints

### Get All Users
```
GET /api/users?page=0&size=20
```
Returns a paginated list of users.

### Get User by ID
```
GET /api/users/{id}
```
Returns a specific user by ID.

### Get User by Email
```
GET /api/users/email/{email}
```
Returns a specific user by email address.

### Create User
```
POST /api/users
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123",
    "name": "John Doe"
}
```
Creates a new user.

### Update User
```
PUT /api/users/{id}
Content-Type: application/json

{
    "email": "newemail@example.com",
    "name": "Jane Doe"
}
```
Updates an existing user. All fields are optional.

### Delete User
```
DELETE /api/users/{id}
```
Deletes a user by ID.

## Data Models

### User Entity
```kotlin
data class User(
    val id: Long = 0,
    val email: String,
    val password: String,
    val name: String
)
```

### UserDto (Response)
```kotlin
data class UserDto(
    val id: Long = 0,
    val email: String,
    val name: String
)
```

### CreateUserRequest
```kotlin
data class CreateUserRequest(
    val email: String,      // Required, must be valid email
    val password: String,   // Required, minimum 6 characters
    val name: String        // Required, 2-50 characters
)
```

### UpdateUserRequest
```kotlin
data class UpdateUserRequest(
    val email: String? = null,  // Optional, must be valid email if provided
    val name: String? = null    // Optional, 2-50 characters if provided
)
```

## Error Responses

The API returns structured error responses:

```json
{
    "timestamp": "2024-01-01T12:00:00",
    "status": 404,
    "error": "Not Found",
    "message": "User not found with id: 1",
    "path": "/api/users"
}
```

### HTTP Status Codes
- `200 OK` - Successful GET, PUT requests
- `201 Created` - Successful POST request
- `204 No Content` - Successful DELETE request
- `400 Bad Request` - Validation errors
- `404 Not Found` - User not found
- `409 Conflict` - User already exists (duplicate email)
- `500 Internal Server Error` - Unexpected errors

## Running the Application

### Prerequisites
- Java 21
- Gradle

### Build and Run
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### H2 Database Console
Access the H2 database console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

### Running Tests
```bash
./gradlew test
```

## Example Usage

### Create a user
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123",
    "name": "John Doe"
  }'
```

### Get all users
```bash
curl http://localhost:8080/api/users
```

### Get user by ID
```bash
curl http://localhost:8080/api/users/1
```

### Update user
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith"
  }'
```

### Delete user
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## Project Structure

```
src/
├── main/kotlin/io/dhlottery/user/
│   ├── api/user_management_api/
│   │   └── UserManagementApiApplication.kt
│   ├── config/
│   │   └── SecurityConfig.kt
│   ├── controller/
│   │   └── UserController.kt
│   ├── dto/
│   │   └── UserDto.kt
│   ├── entity/
│   │   └── User.kt
│   ├── exception/
│   │   └── GlobalExceptionHandler.kt
│   ├── repository/
│   │   └── UserRepository.kt
│   └── service/
│       └── UserService.kt
└── test/kotlin/io/dhlottery/user/
    └── controller/
        └── UserControllerTest.kt
``` 
