package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {

    /**
     * String > Integer 타입으로 변환하는 컨버터
     *
     * @param source
     * @return
     */
    @Override
    public Integer convert(String source) {
        log.info("convert soruce={}", source);

        return Integer.valueOf(source);
    }
}
