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

1. 타입 Converter를 하나하나 직접 찾아서 타입 변환에 사용하는 것은 매우 불편하다.
2. 스프링은 개별 Converter를 모아두고 그것들을 묶어서 편리하게 사용할 수 있는 기능을 제공하는데, 이것이 바로 `ConversionService`이다.
3. ConversionService는 단순히 컨버팅이 가능한지 확인하는 기능과, 컨버팅 기능을 제공한다.
   ```java
   DefaultConversionService conversionService = new DefaultConversionService();
   conversionService.addConverter(new StringToIntegerConverter());
   conversionService.addConverter(new IntegerToStringConverter());
   conversionService.addConverter(new StringToIpPortConverter());
   conversionService.addConverter(new IpPortToStringConverter());
   
   //문자 값을 숫자 값으로 변환하는 경우(StringToIntegerConverter가 동작)
   Integer result = conversionService.convert("10", Integer.class);
   ```
4. 위 예제 코드에서 사용한 `DefaultConversionService`는 ConversionService 인터페이스를 구현했는데, 추가로 Converter를 등록하는 기능도 제공한다.

### 인터페이스 분리 원칙 - ISP(Interface Segregation Principal)

1. 인터페이스 분리 원칙은 클라이언트가 자신이 이용하지 않는 메소드가 의존하지 않아야 한다.
    - DefaultConversionService는 다음 두 인터 페이스를 구현했다.
        - ConversionService: 컨버터 사용에 초점
        - ConverterRegistry: 컨버터 등록에 초점
    - 이렇게 인터페이스를 분리하면 컨버터를 사용하는 클라이언트와 컨버터를 등록하고 관리하는 클라이언트의 관심사를 명확하게 분리할 수 있다.
        - 특히 컨버터를 사용하는 클라이언트는 ConversionService만 의존하면 되므로, 컨버터를 어떻게 등록하고 관리하는지는 전혀 몰라도 된다.
        - 결과적으로 컨버터를 사용하는 클라이언트는 꼭 필요한 메소드만 알면 된다. 이렇게 인터페이스를 분리하는 것을 ISP라 한다.

## 스프링에 Converter 적용하기

1. 스프링은 내부에서 `ConversionService`를 제공한다.
2. 개발자가 직접 구현한 컨버터를 추가 등록하고 싶은 경우 `WebMvcConfigurer`가 제공하는 `addFormatter()`를 사용해서 추가하고 싶은 컨버터를 등록하면 된다.
    - 이렇게 하면 스프링은 내부에서 사용하는 `ConversionService`에 컨버터를 추가해준다.
3. 스프링은 내부에서 수 많은 기본 컨버터들을 제공하는데, 동일 기능을 하는 컨버터가 추가 등록되 었다 `추가한 컨버터가 기본 컨버터 보다 높은 우선순위를 가진다.`

### 처리 과정

1. `@RequestParam`은 `@RequestParam`을 처리하는 `ArgumentResolver`인 `RequestParamArgumentResolver`에서 `ConversionService`를 사용해서
   타입을 변환한다.

## 뷰 템플릿에 컨버터 적용하기

1. 타임리프는 렌더링 시에 컨버터를 적용해서 렌더링 하는 방법을 편리하게 지원한다.
2. 타임리프에서 `${{...}}`를 사용하면 자동으로 `ConversionService`를 사용해서 변환된 결과를 출력한다.
    - 물론 스프링과 통합되어 스프링이 제공하는 ConversionService를 사용하므로, 개발자가 추가로 등록한 컨버터들을 사용할 수 있다.
    - 타임리프 변수 표현식: `${...}`
    - 타임리프 ConversionService 적용: `${{...}}`

### 참고

   ```java

@Controller
public class ConverterController {

    @GetMapping("/converter-view")
    public String converterView(Model model) {
        model.addAttribute("number", 10000);
        model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080));

        return "converter-view";
    }
}
   ```

1. 뷰 템플릿은 데이터를 문자로 출력한다. 따라서 다음과 같이 컨버터를 적용하게 되면 Integer 타입인 10000을 String으로 변환하는 컨버터를 실행하게 된다.
    - 여기서는 `IntegerToStringConverter`가 적용된다.
2. 뷰 템플릿은 데이터를 문자로 출력한다. 따라서 다음과 같이 컨버터를 적용하게 되면 IpPort 타입을 String으로 변환하는 컨버터를 실행하게 된다.
    - 여기서는 `IpPortToStringConverter`가 적용된다.

### 폼에 이용하기

1. 타임리프의 `th:field`는 id, name를 출력하는 등 다양한 기능이 있는데, 여기에 ConversionService도 함께 적용된다.

## 포맷터 - Formatter

1. `Converter`는 입력과 출력 타입에 제한이 없는 범용 타입 변환 기능을 제공한다.
2. 객체를 특정한 포맷에 맞추어 문자로 출력하거나, 또는 그 반대의 역할을 하는 것에 특화된 기능이 바로 `Formatter`이다.
    - 화면에 숫자를 출력해야 하는데, `Integer > String` 출력 시점에 `숫자 1000 > 문자 "1,000"` 이렇게 1000 단위에 쉼표를 넣을 때
    - 날짜 객체를 문자인 "2021-01-01 10:50:11"와 같이 출력하거나 그 반대인 상황일 때

### Converter VS Formatter

1. Converter는 범용(객체 > 객체)
2. Formatter는 문자에 특화(객체 > 문자, 문자 > 객체) + 현지화(Locale)
    - Converter의 특별한 버전

### 포맷터 만들기

1. 포맷터는 객체를 문자로 변환하고, 문자를 객체로 변환하는 두 가지 기능을 모두 수행한다.
    - String print(T object, Locale locale): 객체를 문자로 변환한다.
    - T parse(String text, Locale locale): 문자를 객체로 변환한다.
