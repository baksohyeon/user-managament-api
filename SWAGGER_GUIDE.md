# Swagger API Documentation Guide

## 개요
User Management API에 대한 완전한 Swagger/OpenAPI 3.0 문서가 구현되었습니다.

## 접근 방법

### 1. Swagger UI
- **URL**: http://localhost:8080/swagger-ui.html
- **설명**: 대화형 API 문서 인터페이스
- **기능**: 
  - API 엔드포인트 탐색
  - 실시간 API 테스트
  - 요청/응답 예제 확인
  - 스키마 정의 확인

### 2. OpenAPI JSON
- **URL**: http://localhost:8080/api-docs
- **설명**: OpenAPI 3.0 스펙 JSON 형식
- **용도**: API 클라이언트 생성, 외부 도구 연동

## 주요 기능

### API 엔드포인트
- `GET /api/users` - 전체 사용자 조회 (페이지네이션 지원)
- `GET /api/users/{id}` - ID로 사용자 조회
- `GET /api/users/email/{email}` - 이메일로 사용자 조회
- `POST /api/users` - 새 사용자 생성
- `PUT /api/users/{id}` - 사용자 정보 수정
- `DELETE /api/users/{id}` - 사용자 삭제

### 문서화된 요소
- ✅ 모든 엔드포인트에 대한 상세 설명
- ✅ 요청/응답 스키마 정의
- ✅ HTTP 상태 코드별 응답 예제
- ✅ 입력 파라미터 검증 규칙
- ✅ 에러 응답 형식 정의
- ✅ 실제 사용 예제

### 에러 처리
- **400**: 잘못된 요청 데이터 (ValidationErrorResponse)
- **404**: 리소스를 찾을 수 없음 (ErrorResponse)
- **409**: 중복된 리소스 (ErrorResponse)
- **500**: 서버 내부 오류 (ErrorResponse)

## 사용 방법

### 1. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 2. Swagger UI 접속
브라우저에서 http://localhost:8080/swagger-ui.html 접속

### 3. API 테스트
1. 원하는 엔드포인트 선택
2. "Try it out" 버튼 클릭
3. 필요한 파라미터 입력
4. "Execute" 버튼 클릭
5. 응답 확인

## 설정 정보

### application.yaml 설정
```yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    try-it-out-enabled: true
    operations-sorter: alpha
    tags-sorter: alpha
    doc-expansion: none
  api-docs:
    path: /api-docs
    enabled: true
  show-actuator: false
```

### 의존성
```kotlin
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
implementation("org.springdoc:springdoc-openapi-starter-common:2.3.0")
```

## 추가 정보

### API 정보
- **제목**: User Management API
- **버전**: 1.0.0
- **라이선스**: MIT License
- **연락처**: dev@dhlottery.io

### 서버 정보
- **개발 서버**: http://localhost:8080
- **프로덕션 서버**: https://api.dhlottery.io

## 문제 해결

### Swagger UI가 로드되지 않는 경우
1. 애플리케이션이 정상적으로 실행되었는지 확인
2. 포트 8080이 사용 중인지 확인
3. 브라우저 캐시 삭제 후 재시도

### API 테스트 실패 시
1. 요청 데이터 형식 확인
2. 필수 필드 누락 여부 확인
3. 데이터 타입 일치 여부 확인
4. 서버 로그 확인 