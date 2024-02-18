package com.artino.service.desensitization;

import com.artino.service.utils.SpringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class DesensitizationSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private Desensitization desensitization;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(desensitize(s));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                Desensitization desensitization = beanProperty.getAnnotation(Desensitization.class);
                if (desensitization == null) {
                    desensitization = beanProperty.getContextAnnotation(Desensitization.class);
                }
                if (desensitization != null) {
                    return new DesensitizationSerializer(desensitization);
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }


    private String desensitize(String s) {
        if (Objects.isNull(s) || s.isEmpty() || s.isBlank()) return s;
        Desensitization.DesensitizationType type = desensitization.type();
        return switch (type) {
            case PHONE -> desensitizeWithPhone(s);
            case EMAIL -> desensitizeWithEmail(s);
            case ID -> desensitizeWithID(s);
            case FILE -> desensitizeWithFile(s);
            case BANKCARD -> desensitizeWithBankCard(s);
            default -> s;
        };
    }


    private String desensitizeWithPhone(String s) {
        return s.replaceAll("(\\d{3})\\d{4}(\\d{4})",
                String.format("$1%s%s%s%s$2", desensitization.mask(), desensitization.mask(), desensitization.mask(), desensitization.mask())
        );
    }

    private String desensitizeWithEmail(String s) {
        return s.replaceAll("(^\\w)[^@]*(@.*$)",
                String.format("$1%s%s%s%s$2", desensitization.mask(), desensitization.mask(), desensitization.mask(), desensitization.mask())
        );
    }

    private String desensitizeWithID(String s) {
        return s.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", desensitization.mask());
    }

    private String desensitizeWithFile(String s) {
        Environment environment = SpringUtils.getBean(Environment.class);
        String host = environment.getProperty("constant.remote.host");
        if (StringUtils.isEmpty(host)) return s;
        return String.format("%s/%s", host, s);
    }

    private String desensitizeWithBankCard(String s) {
        return s.replaceAll("(\\d{6})\\d{9}(\\d{4})",
                String.format("$1%s%s%s%s$2", desensitization.mask(), desensitization.mask(), desensitization.mask(), desensitization.mask())
        );
    }

}
