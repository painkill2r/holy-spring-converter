package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * IpPort 객체를 문자열(127.0.0.1:8080)로 변환하는 컨버터
 */
@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {

    @Override
    public String convert(IpPort source) {
        log.info("convert source={}", source);

        return source.getIp() + ":" + source.getPort();
    }
}
