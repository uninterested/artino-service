package com.artino.service.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class ServletUtils {
    public static ServletRequestAttributes requestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 获取当前的请求实例
     *
     * @return 当前的请求
     */
    public static HttpServletRequest currentRequest() {
        return requestAttributes().getRequest();
    }

    /**
     * 获取当前的响应实例
     * @return 当前的响应
     */
    public static HttpServletResponse currentResponse() {
        return requestAttributes().getResponse();
    }


    /**
     * 获取请求头的value
     * @param name header name
     * @return header value
     */
    public static String getHeaderParameter(String name) {
        return currentRequest().getHeader(name);
    }

    /**
     * 获取请求pathname
     * @return pathname
     */
    public static String getRequestURI() {
        HttpServletRequest request = currentRequest();
        return request.getRequestURI();
    }
}
