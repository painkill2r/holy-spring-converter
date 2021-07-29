package hello.typeconverter.controller;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class FormatterController {

    /**
     * 10000, LocalDateTime.now()를 문자로 변환해서 넣어줌
     *
     * @param model
     * @return
     */
    @GetMapping("/formatter/edit")
    public String formatterForm(Model model) {
        Form form = new Form();
        form.setNumber(10000);
        form.setLocalDateTime(LocalDateTime.now());

        model.addAttribute("form", form);

        return "formatter-form";
    }

    /**
     * "10,000"
     * "2021-07-29 23:45:20"
     * 위와 같은 문자를 객체로 변환해서 넣어줌
     *
     * @param form
     * @return
     */
    @PostMapping("/formatter/edit")
    public String formatterEdit(@ModelAttribute Form form) {
        return "formatter-view";
    }

    @Data
    static class Form {
        @NumberFormat(pattern = "###,###") //스프링 포맷터들을 적용
        private Integer number;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //스프링 포맷터들을 적용
        private LocalDateTime localDateTime;
    }
}
