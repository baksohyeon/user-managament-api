package io.dhlottery.user.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("User Management API")
                    .description("""
                        사용자 관리를 위한 REST API 문서
                        
                        ## 주요 기능
                        - 사용자 생성, 조회, 수정, 삭제 (CRUD)
                        - 페이지네이션을 지원하는 사용자 목록 조회
                        - 이메일 기반 사용자 검색
                        - 입력 데이터 유효성 검증
                        
                        ## 인증
                        현재 버전에서는 인증이 구현되어 있지 않습니다.
                        
                        ## 에러 처리
                        - 400: 잘못된 요청 데이터
                        - 404: 리소스를 찾을 수 없음
                        - 409: 중복된 리소스 (이메일)
                        - 500: 서버 내부 오류
                    """.trimIndent())
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("DH Lottery Development Team")
                            .email("dev@dhlottery.io")
                            .url("https://dhlottery.io")
                    )
                    .license(
                        License()
                            .name("MIT License")
                            .url("https://opensource.org/licenses/MIT")
                    )
            )
            .servers(
                listOf(
                    Server()
                        .url("http://localhost:8080")
                        .description("Local Development Server"),
                    Server()
                        .url("https://api.dhlottery.io")
                        .description("Production Server")
                )
            )
    }
}