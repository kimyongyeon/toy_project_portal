# 토이프로젝트 포털사이트 백앤드 소스 구조
## 사용한 기술
- 스프링 스타터 웹
- 스프링 데이터 JPA
- 타임리프
- 롬복
- h2
- mysql
- validation
- 스웨거 
- common lang3
- gson

## 차후계획
- SwaggerUI 붙여서 API목록을 화면으로 제공

## 사용한 라이브러리
``` 
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>2.0.1.Final</version>
</dependency>

<!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger2 -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>

<!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger-ui -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.0.2</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.apache.commons/commons-lang3 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.9</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.5</version>
</dependency>

```

## maven 정리 
```$xslt
mvn clean package 
```

## 테스트
```$xslt
###
POST http://localhost:8081/board
Content-Type: application/json

{
  "title": "content",
  "contents": "contents"
}

<> 2020-06-07T112026.200.txt

###
PUT http://localhost:8081/board
Content-Type: application/json

{
  "title": "content",
  "contents": "contents"
}

<> 2020-06-07T112019.200.txt

###
DELETE http://localhost:8081/board
Content-Type: application/json

{
  "title": "content",
  "contents": "contents"
}

<> 2020-06-07T112021.200.txt
``` 

## 사용한 툴 
- Intellij

## 배포 
- Docker
 
