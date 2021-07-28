package hello.typeconverter.fomatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * 숫자를 1000 단위로 쉼표가 들어가는 문자로 변환해주는 포맷터
 * - 그 반대로도 처리해주는 기능을 갖는다.
 */
@Slf4j
public class MyNumberFomatter implements Formatter<Number> {

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("text={}, locale={}", text, locale);
        //"1,000" > 1000
        return NumberFormat.getInstance(locale).parse(text);
    }

    @Override
    public String print(Number object, Locale locale) {
        log.info("object={}, locale={}", object, locale);
        //1000 > "1,000"
        return NumberFormat.getInstance(locale).format(object);
    }
}
