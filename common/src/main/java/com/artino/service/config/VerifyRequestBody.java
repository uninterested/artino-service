package com.artino.service.config;

import com.artino.service.base.BusinessException;
import com.artino.service.utils.CryptoUtils;
import com.artino.service.utils.SpringUtils;
import com.artino.service.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@ControllerAdvice
public class VerifyRequestBody implements RequestBodyAdvice {
    @Override
    public boolean supports(@NotNull MethodParameter methodParameter, @NotNull Type type, @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, @NotNull MethodParameter a, @NotNull Type b, @NotNull Class<? extends HttpMessageConverter<?>> c) {
        Environment env = SpringUtils.getBean(Environment.class);
        String verify = env.getProperty("constant.verify.status", "false");
        String key = env.getProperty("constant.verify.key", "");
        if ("false".equals(verify)) return inputMessage;
        return verify(inputMessage, key);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }


    private HttpInputMessage verify(HttpInputMessage inputMessage, String key) {
        try {
            String sign = inputMessage.getHeaders().getFirst("X-Sign");
            if (Objects.isNull(sign) || sign.isEmpty() || sign.isBlank()) throw BusinessException.build(199990, "参数签名丢失");
            InputStream is = inputMessage.getBody();
            byte[] data = new byte[is.available()];
            is.read(data);
            String body = new String(data, StandardCharsets.UTF_8);
            String selfSign = CryptoUtils.md5Encode(body + key);
            if (!selfSign.equals(sign.toLowerCase()))
                throw BusinessException.build(199991, "参数签名错误，请检查请求是否被串改");
            return new HttpInputMessage() {
                @NotNull
                @Override
                public InputStream getBody() throws IOException {
                    return new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
                }

                @NotNull
                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (IOException e) {
            return null;
        }
    }

}
