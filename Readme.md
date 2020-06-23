# 토이프로젝트 포털사이트 백앤드 소스 구조
## 사용한 기술
- Spring-WebMvc
- Spring-Data-JPA
- Spring-security
- 타임리프
- lombok
- h2
- mysql
- validation
- swagger-ui
- common lang3
- gson
- queryDSL 4.3.1
- websocket
- sockjs
- stomp
- Jsaypt : 암호화 적용

## 개발 표준 가이드 
- 트랜잭션 기능에 대한 메서드는 Transaction을 붙여준다.  
예) setLikeTransaction, setDisLikeTransaction  

- 비슷한 기능은 Base interface를 만들고 상속받아 개발한다.  
예) BaseService => BoardService implements BaseService  

- 트랜잭션이 필요한 메서드는 반드시 부모에 @Transactional을 붙이도록 한다.    
예) @Transactional setLikeAndDisLike => setLikeTransaction(CRUD-CODE)  
  
- 상수 정의는 해당 package 폴더안에 xxxConst.java 파일로 만들어 static+대문자로 만든다.  
예) public static final String FAIL_REQUIRED_VALUE = "필수 값을 입력 하시오.";    

- 비즈니스 Exception은 exception 패키지를 만들고 RuntimeException을 상속받아 Exception class를 만들고 throw 던질때 사용한다.  




## sockjs 메모리 아키텍처
![sockjs architecture](https://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/images/message-flow-simple-broker.png)

## sockjs + amazonMQ 아키텍처
![sockjs architecture](https://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/images/message-flow-broker-relay.png)

## 컴파일 방법 및 실행방법
mvn compile
mvn package
mvn spring-boot:run


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
 
