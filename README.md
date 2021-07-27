#### 최초 작성일 : 2021.07.24(토)

# Spring Boot Type Converter

Spring Type Converter 학습

## 학습 환경

1. OS : MacOS
2. JDK : OpenJDK 11.0.5
3. Framework : Spring Boot 2.5.3
    - [Spring Initializer 링크 : https://start.spring.io](https://start.spring.io)
    - 패키징 : jar
    - 의존설정(Dependencies)
        - Spring Web
        - Thymeleaf
        - Lombok
4. Build Tools : Gradle

## API 예외 처리

1. 지금까지 HTML을 사용한 오류 페이지는 단순히 고객에게 오류 화면을 보여주고 끝이 났다.
    - 4xx, 5xx HTTML 오류 페이지
2. 하지만 API는 각 오류 상황에 맞는 `오류 응답 스펙`을 정하고, JSON으로 데이터를 내려주어야 한다.
    - 클라이언트는 정상 요청이든, 오류 요청이든 JSON이 반환되기를 기대한다.

## 스프링 타입 컨버터

1. 문자를 숫자로 변환하거나, 반대로 숫자를 문자로 변환해야 하는 것 처럼 애플리케이션을 개발하다 보면 타입을 변환해야 하는 경우가 상당히 많다.
   ```java
   @RestController
   public class HelloController {
        @GetMapping("/hello-v1")
        public String helloV1(HttpServletRequest request) {
            String data = request.getParameter("data"); //문자 타입 조회
            Integer intValue = Integer.valueOf(data); //숫자 타입으로 변경
   
            System.out.println("intValue = " + intValue);
   
            return "ok";
        }
   }
   ```
    - HTTP 요청 파라미터는 모두 문자로 처리된다. 따라서 위 예제 코드에서 처럼 다른 타입으로 변환해서 사용하고 싶으면 타입 변환 과정을 거쳐야 한다.
2. 이번에는 스프링 MVC가 제공하는 @RequestParam을 사용해 보자.
   ```java   
   @RestController
   public class HelloController {
      @GetMapping("/hello-v2")
      public String helloV2(@RequestParam Integer data) {
           System.out.println("data = " + data);
           return "ok"; 
      }
   }
   ```
    - 스프링이 제공하는 @RequestParam을 사용하면 문자열로 들어오는 요청 파라미터를 메소드 인자 타입에 맞게 자동으로 변환해서 사용할 수 있게 한다.
    - `이것은 스프링이 중간에서 타입 변환기를 사용해서 타입을 변환해 주었기 때문이다.`
        - 이러한 예는 @PathVarible, @ModelAttribute에서도 확인할 수 있다.

### 스프링의 타입 변환 적용 예

1. 스프링 MVC 요청 파라미터
    - `@RequestParam`, `@ModelAttribute`, `@PathVariable`
2. `@Value` 등으로 YML 정보 읽기
3. XML에 넣은 스프링 빈 정보를 반환
4. 뷰를 렌더링 할 때

## Converter 인터페이스

1. 만약 개발자가 새로운 타입을 만들어서 변환하고 싶은 경우 스프링은 확장 가능한 `Converter` 인터페이스를 제공한다.
    - 개발자는 스프링에 추가적인 타입 변환이 필요하면 이 Converter 인터페이스를 구현해서 스프링 Bean으로 등록하면 된다.
        - `org.springframework.core.convert.converter.Converter 인터페이스 구현`
    - 이 Converter 인터페이스는 모든 타입에 적용할 수 있다.
2. 필요하면 `X > Y` 타입으로 변환하는 Converter 인터페이스를 만들고, 또 `Y > X` 타입으로 변환하는 Converter 인터페이스를 만들어서 등록하면 된다.
    - 예를 들어서 문자열로 `"true"`가 오면 `Boolean` 타입으로 받고 싶으면 `String > Boolean` 타입으로 변환되도록 Converter 인터페이스를 만들어서 등록하고, 반대로 적용하고
      싶으면 `Boolean > String` 타입으로 변환되도록 Converter 인터페이스를 추가로 만들어서 등록하면 된다.
3. 그런데 이렇게 타입 Converter를 하나하나 직접 사용하면, 개발자가 직접 컨버팅 하는 거 ㅅ과 큰 차이가 없다.
    - 따라서 타입 Converter를 등록하고 관리하면서 편리하게 변환 기능을 제공하는 역할을 하는 무언가가 필요하다.
    - `ConversionService`

### 용도에 따른 다양한 타입 컨버터

1. Converter: 기본 타입 컨버터
2. ConverterFactory: 전체 클래스 계층 구조가 필요할 때
3. GenericConverter: 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능
4. ConditionalGenericConverter: 특정 조건이 참인 경우에만 실행

### 참고

1. 스프링은 문자, 숫자, Boolean, Enum 등 일반적인 타입에 대한 대부분의 컨버터를 기본으로 제공한다.
    - `Converter`, `ConverterFactory`, `GenericConverter`의 구현체를 찾아보면 수 많은 컨버터를 확인할 수 있다.

## ConversionService

1. 
   