# 토이프로젝트 포털사이트 백앤드 소스 구조
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
