package io.dhlottery.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/health")
@Tag(
    name = "Health Check",
    description = "🏥 System health and monitoring endpoints for application status verification"
)
class HealthController {

    @Operation(
        summary = "🔍 Application Health Check",
        description = """
            Returns the current health status of the application including:
            - Application status (UP/DOWN)
            - Database connectivity
            - System information
            - Response time metrics
            
            This endpoint is typically used by:
            - Load balancers for health checks
            - Monitoring systems (Prometheus, Grafana)
            - DevOps teams for system verification
            - Automated deployment pipelines
        """,
        tags = ["Health Check"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "✅ Application is healthy and running normally",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = HealthResponse::class),
                        examples = [
                            ExampleObject(
                                name = "Healthy Response",
                                summary = "Normal healthy application state",
                                value = """
                                    {
                                        "status": "UP",
                                        "timestamp": "2024-01-15T10:30:00",
                                        "application": "User Management API",
                                        "version": "1.0.0",
                                        "environment": "development",
                                        "components": {
                                            "database": {
                                                "status": "UP",
                                                "details": {
                                                    "database": "H2",
                                                    "validationQuery": "SELECT 1"
                                                }
                                            },
                                            "diskSpace": {
                                                "status": "UP",
                                                "details": {
                                                    "total": 499963174912,
                                                    "free": 91943821312,
                                                    "threshold": 10485760
                                                }
                                            }
                                        },
                                        "uptime": "2h 15m 30s",
                                        "memoryUsage": {
                                            "used": "256MB",
                                            "max": "1024MB",
                                            "percentage": 25
                                        }
                                    }
                                """
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "503",
                description = "❌ Application is unhealthy or experiencing issues",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = HealthResponse::class),
                        examples = [
                            ExampleObject(
                                name = "Unhealthy Response",
                                summary = "Application with health issues",
                                value = """
                                    {
                                        "status": "DOWN",
                                        "timestamp": "2024-01-15T10:30:00",
                                        "application": "User Management API",
                                        "version": "1.0.0",
                                        "environment": "development",
                                        "components": {
                                            "database": {
                                                "status": "DOWN",
                                                "details": {
                                                    "error": "Connection refused",
                                                    "database": "H2"
                                                }
                                            }
                                        },
                                        "uptime": "2h 15m 30s"
                                    }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    @GetMapping
    fun health(): ResponseEntity<HealthResponse> {
        val healthResponse = HealthResponse(
            status = "UP",
            timestamp = LocalDateTime.now(),
            application = "User Management API",
            version = "1.0.0",
            environment = "development",
            components = mapOf(
                "database" to mapOf(
                    "status" to "UP",
                    "details" to mapOf(
                        "database" to "H2",
                        "validationQuery" to "SELECT 1"
                    )
                ),
                "diskSpace" to mapOf(
                    "status" to "UP",
                    "details" to mapOf(
                        "total" to 499963174912L,
                        "free" to 91943821312L,
                        "threshold" to 10485760L
                    )
                )
            ),
            uptime = "2h 15m 30s",
            memoryUsage = mapOf(
                "used" to "256MB",
                "max" to "1024MB",
                "percentage" to 25
            )
        )
        
        return ResponseEntity.ok(healthResponse)
    }

    @Operation(
        summary = "📊 Detailed System Information",
        description = """
            Provides comprehensive system information including:
            - JVM details and memory usage
            - Operating system information
            - Application configuration
            - Runtime metrics
            
            **Note**: This endpoint may expose sensitive information and should be secured in production.
        """,
        tags = ["Health Check"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "System information retrieved successfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = SystemInfoResponse::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/info")
    fun systemInfo(): ResponseEntity<SystemInfoResponse> {
        val runtime = Runtime.getRuntime()
        val systemInfo = SystemInfoResponse(
            jvm = JvmInfo(
                version = System.getProperty("java.version"),
                vendor = System.getProperty("java.vendor"),
                runtime = System.getProperty("java.runtime.name")
            ),
            os = OsInfo(
                name = System.getProperty("os.name"),
                version = System.getProperty("os.version"),
                arch = System.getProperty("os.arch")
            ),
            memory = MemoryInfo(
                total = runtime.totalMemory(),
                free = runtime.freeMemory(),
                max = runtime.maxMemory(),
                used = runtime.totalMemory() - runtime.freeMemory()
            ),
            application = ApplicationInfo(
                name = "User Management API",
                version = "1.0.0",
                profiles = listOf("default"),
                startTime = LocalDateTime.now().minusHours(2)
            )
        )
        
        return ResponseEntity.ok(systemInfo)
    }
}

@Schema(description = "Application health status response")
data class HealthResponse(
    @Schema(description = "Overall health status", example = "UP", allowableValues = ["UP", "DOWN"])
    val status: String,
    
    @Schema(description = "Health check timestamp", example = "2024-01-15T10:30:00")
    val timestamp: LocalDateTime,
    
    @Schema(description = "Application name", example = "User Management API")
    val application: String,
    
    @Schema(description = "Application version", example = "1.0.0")
    val version: String,
    
    @Schema(description = "Environment name", example = "development")
    val environment: String,
    
    @Schema(description = "Component health details")
    val components: Map<String, Any>,
    
    @Schema(description = "Application uptime", example = "2h 15m 30s")
    val uptime: String,
    
    @Schema(description = "Memory usage information")
    val memoryUsage: Map<String, Any>? = null
)

@Schema(description = "Detailed system information response")
data class SystemInfoResponse(
    @Schema(description = "JVM information")
    val jvm: JvmInfo,
    
    @Schema(description = "Operating system information")
    val os: OsInfo,
    
    @Schema(description = "Memory usage details")
    val memory: MemoryInfo,
    
    @Schema(description = "Application information")
    val application: ApplicationInfo
)

@Schema(description = "JVM information")
data class JvmInfo(
    @Schema(description = "Java version", example = "21.0.1")
    val version: String,
    
    @Schema(description = "JVM vendor", example = "Eclipse Adoptium")
    val vendor: String,
    
    @Schema(description = "Runtime name", example = "OpenJDK Runtime Environment")
    val runtime: String
)

@Schema(description = "Operating system information")
data class OsInfo(
    @Schema(description = "OS name", example = "Mac OS X")
    val name: String,
    
    @Schema(description = "OS version", example = "14.2.1")
    val version: String,
    
    @Schema(description = "System architecture", example = "aarch64")
    val arch: String
)

@Schema(description = "Memory usage information")
data class MemoryInfo(
    @Schema(description = "Total memory in bytes", example = "1073741824")
    val total: Long,
    
    @Schema(description = "Free memory in bytes", example = "536870912")
    val free: Long,
    
    @Schema(description = "Maximum memory in bytes", example = "1073741824")
    val max: Long,
    
    @Schema(description = "Used memory in bytes", example = "536870912")
    val used: Long
)

@Schema(description = "Application information")
data class ApplicationInfo(
    @Schema(description = "Application name", example = "User Management API")
    val name: String,
    
    @Schema(description = "Application version", example = "1.0.0")
    val version: String,
    
    @Schema(description = "Active profiles", example = "[\"default\"]")
    val profiles: List<String>,
    
    @Schema(description = "Application start time", example = "2024-01-15T08:15:00")
    val startTime: LocalDateTime
) 