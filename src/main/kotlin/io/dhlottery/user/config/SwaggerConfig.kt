package io.dhlottery.user.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.tags.Tag
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.customizers.OperationCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod

@Configuration
class SwaggerConfig {

    @Value("\${spring.application.name:User Management API}")
    private lateinit var applicationName: String

    @Value("\${server.port:8080}")
    private lateinit var serverPort: String

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(apiInfo())
            .servers(serverList())
            .externalDocs(externalDocumentation())
            .tags(tagList())
            .components(securityComponents())
            .security(securityRequirements())
    }

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public-api")
            .displayName("Public API")
            .pathsToMatch("/api/**")
            .pathsToExclude("/api/admin/**", "/api/internal/**")
            .addOpenApiCustomizer(publicApiCustomizer())
            .addOperationCustomizer(operationCustomizer())
            .build()
    }

    @Bean
    fun adminApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("admin-api")
            .displayName("Admin API")
            .pathsToMatch("/api/admin/**")
            .addOpenApiCustomizer(adminApiCustomizer())
            .addOperationCustomizer(operationCustomizer())
            .build()
    }

    @Bean
    fun allApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("all-api")
            .displayName("Complete API")
            .pathsToMatch("/api/**")
            .addOpenApiCustomizer(completeApiCustomizer())
            .addOperationCustomizer(operationCustomizer())
            .build()
    }

    private fun apiInfo(): Info {
        return Info()
            .title("$applicationName - Enhanced Documentation")
            .description("""
                # 🚀 User Management API - Complete Documentation
                
                ## 📋 Overview
                This is a comprehensive REST API for user management built with **Spring Boot 3** and **Kotlin**.
                The API provides full CRUD operations with advanced features like pagination, validation, and error handling.
                
                ## ✨ Key Features
                - 👤 **User Management**: Complete CRUD operations for user entities
                - 📄 **Pagination Support**: Efficient data retrieval with customizable page sizes
                - 🔍 **Advanced Search**: Search users by email with exact matching
                - ✅ **Input Validation**: Comprehensive validation with detailed error messages
                - 🛡️ **Error Handling**: Standardized error responses with proper HTTP status codes
                - 📊 **Data Consistency**: Transaction management for data integrity
                - 🔒 **Security Ready**: Prepared for authentication and authorization
                
                ## 🏗️ Architecture
                - **Framework**: Spring Boot 3.3.12
                - **Language**: Kotlin 1.9.25
                - **Database**: H2 (Development), PostgreSQL (Production Ready)
                - **ORM**: Spring Data JPA with Hibernate
                - **Validation**: Bean Validation (JSR-303)
                - **Documentation**: OpenAPI 3.0 with Swagger UI
                
                ## 📚 API Usage Guidelines
                
                ### Request Format
                - All requests should use `Content-Type: application/json`
                - Request bodies must be valid JSON
                - Required fields are marked in the schema
                
                ### Response Format
                - All responses return JSON
                - Success responses include relevant data
                - Error responses follow standardized format
                
                ### Pagination
                - Use `page` parameter for page number (0-based)
                - Use `size` parameter for page size (default: 20, max: 100)
                - Use `sort` parameter for sorting (e.g., `sort=name,asc`)
                
                ### Error Handling
                - **400**: Bad Request - Invalid input data
                - **404**: Not Found - Resource doesn't exist
                - **409**: Conflict - Duplicate resource (e.g., email)
                - **422**: Unprocessable Entity - Validation errors
                - **500**: Internal Server Error - Server-side issues
                
                ## 🔄 API Versioning
                Current version: **v1.0.0**
                - Breaking changes will increment major version
                - New features increment minor version
                - Bug fixes increment patch version
                
                ## 🚦 Rate Limiting
                - Currently no rate limiting implemented
                - Future versions will include rate limiting
                - Recommended: Max 1000 requests per hour per IP
                
                ## 🔐 Authentication & Authorization
                - **Current**: No authentication required
                - **Planned**: JWT-based authentication
                - **Future**: Role-based access control (RBAC)
                
                ## 📞 Support & Contact
                For technical support, API questions, or feature requests:
                - **Email**: dev@dhlottery.io
                - **Documentation**: See external documentation links
                - **Issues**: Report via project repository
                
                ---
                *Built with ❤️ by DH Lottery Development Team*
            """.trimIndent())
            .version("1.0.0")
            .termsOfService("https://dhlottery.io/terms")
            .contact(
                Contact()
                    .name("DH Lottery Development Team")
                    .email("dev@dhlottery.io")
                    .url("https://dhlottery.io/contact")
            )
            .license(
                License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")
            )
    }

    private fun serverList(): List<Server> {
        return listOf(
            Server()
                .url("http://localhost:$serverPort")
                .description("🔧 Local Development Server"),
            Server()
                .url("https://api-dev.dhlottery.io")
                .description("🧪 Development Environment"),
            Server()
                .url("https://api-staging.dhlottery.io")
                .description("🎭 Staging Environment"),
            Server()
                .url("https://api.dhlottery.io")
                .description("🚀 Production Server")
        )
    }

    private fun externalDocumentation(): ExternalDocumentation {
        return ExternalDocumentation()
            .description("📖 Complete API Documentation & Guides")
            .url("https://docs.dhlottery.io/user-management-api")
    }

    private fun tagList(): List<Tag> {
        return listOf(
            Tag()
                .name("User Management")
                .description("👤 Complete user lifecycle management operations")
                .externalDocs(
                    ExternalDocumentation()
                        .description("User Management Guide")
                        .url("https://docs.dhlottery.io/user-management")
                ),
            Tag()
                .name("Health Check")
                .description("🏥 System health and monitoring endpoints")
                .externalDocs(
                    ExternalDocumentation()
                        .description("Monitoring Guide")
                        .url("https://docs.dhlottery.io/monitoring")
                ),
            Tag()
                .name("Admin Operations")
                .description("⚙️ Administrative operations (Future)")
                .externalDocs(
                    ExternalDocumentation()
                        .description("Admin Guide")
                        .url("https://docs.dhlottery.io/admin")
                )
        )
    }

    private fun securityComponents(): Components {
        return Components()
            .securitySchemes(
                mapOf(
                    "bearerAuth" to SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("🔐 JWT Bearer Token Authentication"),
                    "apiKey" to SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .`in`(SecurityScheme.In.HEADER)
                        .name("X-API-Key")
                        .description("🔑 API Key Authentication"),
                    "basicAuth" to SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")
                        .description("🔒 Basic HTTP Authentication")
                )
            )
    }

    private fun securityRequirements(): List<SecurityRequirement> {
        return listOf(
            SecurityRequirement().addList("bearerAuth"),
            SecurityRequirement().addList("apiKey"),
            SecurityRequirement().addList("basicAuth")
        )
    }

    @Bean
    fun operationCustomizer(): OperationCustomizer {
        return OperationCustomizer { operation, handlerMethod ->
            // Add custom operation enhancements
            operation.addExtension("x-api-version", "1.0.0")
            operation.addExtension("x-rate-limit", "1000/hour")
            
            // Add method-specific enhancements
            when (handlerMethod.method.name) {
                "createUser" -> {
                    operation.addExtension("x-code-samples", listOf(
                        mapOf(
                            "lang" to "curl",
                            "source" to """
                                curl -X POST "http://localhost:8080/api/users" \
                                  -H "Content-Type: application/json" \
                                  -d '{
                                    "email": "user@example.com",
                                    "password": "securePassword123",
                                    "name": "홍길동"
                                  }'
                            """.trimIndent()
                        ),
                        mapOf(
                            "lang" to "javascript",
                            "source" to """
                                const response = await fetch('http://localhost:8080/api/users', {
                                  method: 'POST',
                                  headers: { 'Content-Type': 'application/json' },
                                  body: JSON.stringify({
                                    email: 'user@example.com',
                                    password: 'securePassword123',
                                    name: '홍길동'
                                  })
                                });
                                const user = await response.json();
                            """.trimIndent()
                        )
                    ))
                }
                "getAllUsers" -> {
                    operation.addExtension("x-performance-note", "Optimized with pagination for large datasets")
                }
            }
            
            operation
        }
    }

    private fun publicApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            openApi.info.description = "🌐 Public API endpoints available to all users"
        }
    }

    private fun adminApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            openApi.info.description = "⚙️ Administrative API endpoints (requires admin privileges)"
        }
    }

    private fun completeApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            openApi.info.description = "📚 Complete API documentation including all endpoints"
        }
    }
}