### 📁 프로젝트 개요
- 다양한 인증 방식을 구현하여 인증 시스템의 이해를 돕기 위한 Java 기반의 프로젝트입니다. JWT, OAuth2, SNS 인증 등 여러 인증 방법으로 구성되어 있습니다.
<br/>

### 🛠️ 주요 기능
- JWT 인증: JSON Web Token을 활용한 인증 구현
- OAuth2 인증: 구글, 카카오 등 외부 서비스와의 OAuth2 인증 연동
- SNS 인증: 네이버, 카카오톡 등 SNS 계정을 통한 인증 처리
- 프론트엔드 연동: React 기반의 프론트엔드와의 통합 예제 포함
<br/>

### ⚙️ 기술 스택
- 백엔드: Java, Spring Boot
- 프론트엔드: React
- 빌드 도구: Gradle
- 인증 방식:
  - JWT (Json Web Token)
  - OAuth2
  - SNS 인증 (카카오, 네이버 등)
<br/>

### 📂 디렉토리 구조
```bash
auth-project/
├── frontend/             # React 기반의 프론트엔드 코드
├── jwt-auth/             # JWT 인증 관련 구현
├── oauth2-auth/          # OAuth2 인증 관련 구현
├── sns-auth/             # SNS 인증 관련 구현
├── build.gradle.kts      # Gradle 빌드 설정 파일
└── settings.gradle.kts   # 멀티 모듈 프로젝트 설정

```
