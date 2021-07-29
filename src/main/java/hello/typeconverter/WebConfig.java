package hello.typeconverter;

import hello.typeconverter.converter.IntegerToStringConverter;
import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIntegerConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.fomatter.MyNumberFomatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 추가하고 싶은 컨버터 등록
     * 이렇게 하면 스프링 내부에서 사용하는 ConversionService에 등록해준다.
     * 기본 컨버터보다 높은 우선순위를 가진다.
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        //아래 동일한 기능을 하는 포맷터를 등록할 것이기 때문에 컨버터를 등록하는 구문은 주석처리
        //우선순위는 컨버터가 포맷터보다 높기 때문에 동일한 기능을 하는 컨버터, 포맷터가 있는 경우 컨버터가 적용된다.
        //registry.addConverter(new StringToIntegerConverter());
        //registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        //추가
        registry.addFormatter(new MyNumberFomatter());
    }
}
