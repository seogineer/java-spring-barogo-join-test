## 바로고 실무 과제

### 주요 기능
- 회원 가입
    - ID, 비밀번호, 사용자 이름으로 회원 가입
    - 비밀번호 조건
        - 영어 대문자, 소문자, 숫자, 특수문자 중 3종류 이상으로 12자리 이상의 문자열
- 로그인
    - ID, 비밀번호를 입력받아 로그인
    - 로그인 되면 AccessToken 발급
- 배달 조회
    - 배달 기간 필수 입력
    - 기간 내에 사용자가 주문한 배달의 목록 제공 
- 주문 주소 수정
    - 도착지 주소를 요청 받아 처리
    - 사용자가 변경 가능한 배달인 경우에만 수정이 가능

### 실행 방법
````bash
git clone https://github.com/seogineer/java-spring-barogo-join-test.git
````
1. 인텔리제이 - File - Open... 
    - git clone 명령어로 다운로드 받는 프로젝트를 연다.
2. Run - Run...


### API 문서
- src/docs/asciidoc/index.adoc

### 기술
- Java, Spring Boot, Spring Security, JWT, JPA, H2DB, Lombok, MockMvc RestDocs

### .http 샘플
#### 회원가입
```http request
POST http://localhost:8080/api/users/signup
Content-Type: application/json

{
  "username": "user1",
  "password": "Password1234*",
  "name": "홍길동"
}
```

#### 로그인
````http request
POST http://localhost:8080/api/auth/login
Content-Type: application/x-www-form-urlencoded

username=user1&password=Password1234*
````

#### 주문 생성
````http request
POST http://localhost:8080/api/deliveries/create
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW9naW5lZXIiLCJpYXQiOjE3NDIxMzU3MTgsImV4cCI6MTc0MjEzOTMxOH0.tF7fue0flvb0afPVBYZCTrWnmyrwCJgbalzYMx75p8M
Content-Type: application/json

{
  "deliveryAddress": "서울시 강남구 테헤란로 123",
  "deliveryDate": "2025-03-16T10:30:00"
}
````

#### 기간 내 조회
````http request
GET http://localhost:8080/api/deliveries/search?startDate=2025-03-15&endDate=2025-03-16
Authorization: Bearer {AccessToken}
Content-Type: application/json
````

#### 배달 주소 수정
````http request
PUT http://localhost:8080/api/deliveries/1/address?newAddress=서울+관악구+쑥고개로
Authorization: Bearer {AccessToken}
Content-Type: application/json
````
