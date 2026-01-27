# moneymate
토이프로젝트 - 가계부 웹

Spring Boot와 MariaDB를 활용한 월별 가계부 관리 웹 애플리케이션입니다.

## 주요 기능

- 💵 **저축 관리**: 월별 저축 내역 기록 및 조회
- 💸 **지출 관리**: 월별 지출 내역 기록 및 조회 (카테고리별 분류)
- 📝 **메모**: 월별 메모 작성 및 저장
- 📊 **요약**: 월별 총 저축, 총 지출, 잔액 자동 계산

## 기술 스택

- **Backend**: Spring Boot 3.2.0
- **Database**: MariaDB
- **ORM**: Spring Data JPA
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Java Version**: 17

## 프로젝트 구조

```
moneymate/
├── src/
│   ├── main/
│   │   ├── java/com/moneymate/
│   │   │   ├── entity/          # 엔티티 클래스 (Saving, Expense, Memo)
│   │   │   ├── repository/      # 리포지토리 인터페이스
│   │   │   ├── service/         # 비즈니스 로직
│   │   │   ├── controller/      # 컨트롤러
│   │   │   └── MoneyMateApplication.java
│   │   └── resources/
│   │       ├── templates/       # Thymeleaf 템플릿
│   │       └── application.yml  # 설정 파일
└── pom.xml
```

## 설정 방법

### 1. MariaDB 설치 및 데이터베이스 생성

```sql
CREATE DATABASE moneymate CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. application.yml 설정

`src/main/resources/application.yml` 파일에서 데이터베이스 연결 정보를 수정하세요:

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/moneymate?useUnicode=true&characterEncoding=utf8
    username: your_username
    password: your_password
```

### 3. 애플리케이션 실행

```bash
# Maven으로 빌드 및 실행
mvn spring-boot:run
```

또는 IDE에서 `MoneyMateApplication.java`를 실행하세요.

### 4. 웹 브라우저에서 접속

http://localhost:8080

## 데이터베이스 스키마

애플리케이션 실행 시 JPA가 자동으로 다음 테이블을 생성합니다:

- **savings**: 저축 내역
- **expenses**: 지출 내역
- **memos**: 메모

## 사용 방법

1. **월 선택**: 상단에서 조회할 년도와 월을 선택합니다.
2. **저축 추가**: 저축 섹션에서 금액, 설명, 날짜를 입력하여 저축 내역을 추가합니다.
3. **지출 추가**: 지출 섹션에서 금액, 카테고리, 설명, 날짜를 입력하여 지출 내역을 추가합니다.
4. **메모 작성**: 메모 섹션에서 해당 월의 메모를 작성하고 저장합니다.
5. **요약 확인**: 상단의 요약 카드에서 총 저축, 총 지출, 잔액을 확인할 수 있습니다.

## ERD
![Untitled](https://github.com/user-attachments/assets/2016cd42-9079-492e-b675-2ac62b359974)
