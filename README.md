# 포토그램 - 인스타그램 클론 코딩

## 국비지원 강의를 통한 인스타그램 클론 코딩
+ 강의 수강 완료 후의 작업들

1. 네이버 아이디로 로그인(네아로) 구현
- 네이버 아이디로 로그인 시 자동적으로 회원가입되도록 기능 추가
2. 카카오 지도 api 활용
- 카카오 지도 api를 활용해 검색 단어 입력시 관광 명소 리스트 출력되도록 기능 추가
- 해당 명소명이나 마크 클릭시 리스트에 추가되도록 수정
3. 등록하기 버튼 클릭시 일정테이블에 데이터 insert
4. 목록 출력 후 좋아요, 댓글 기능 추가
5. 이미지, 일정 삭제 기능이 없어 추가
6. principalId값으로 체크 해 본인이 계정이 아닐 시 수정이나 삭제 버튼 hidden, 동작되지 못 하도록 전체적 수정


-------------------------------------------------------------------------------------------------------


### 개발에 사용된 기술

- Sring Boot DevTools
- Lombok
- Spring Data JPA
- MariaDB Driver
- Spring Security
- Spring Web
- oauth2-client



-------------------------------------------------------------------------------------------------------


## 설정파일

```xml
<!-- 시큐리티 태그 라이브러리 -->
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-taglibs</artifactId>
</dependency>

<!-- JSP 템플릿 엔진 -->
<dependency>
	<groupId>org.apache.tomcat</groupId>
	<artifactId>tomcat-jasper</artifactId>
	<version>9.0.43</version>
</dependency>

<!-- JSTL -->
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>jstl</artifactId>
</dependency>
```

### 데이터베이스

```sql
create user 'cos'@'%' identified by 'cos1234';
GRANT ALL PRIVILEGES ON *.* TO 'cos'@'%';
create database photogram;
```

### yml 설정

```yml
server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      charset: utf-8
      enabled: true
    
spring:
  mvc:
    view:
      prefix: /WEB-INF/views/
      suffix: .jsp
      
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://localhost:3306/cos?serverTimezone=Asia/Seoul
    username: cos
    password: cos1234
    
  jpa:
    open-in-view: true
    hibernate:
      ddl-auto: update
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: true
      
  servlet:
    multipart:
      enabled: true
      max-file-size: 2MB

  security:
    user:
      name: test
      password: 1234   

file:
  path: C:/src/springbootwork-sts/upload/
```

### 태그라이브러리

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
```
