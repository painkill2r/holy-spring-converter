package hello.typeconverter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    /**
     * 직접 요청 파라미터 타입 변환
     *
     * @param request
     * @return
     */
    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data"); //문자 타입 조회
        Integer intValue = Integer.valueOf(data); //숫자 타입으로 변경

        System.out.println("intValue = " + intValue);

        return "ok";
    }

    /**
     * 스프링이 제공하는 @RequestParam 어노테이션을 사용하여 요청 파라미터 타입 변환
     *
     * @param data
     * @return
     */
    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        System.out.println("data = " + data);

        return "ok";
    }
}
