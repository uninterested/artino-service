package com.artino.service.base;

import com.artino.service.utils.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

public class BusinessException extends  RuntimeException{
    /**
     * 错误码
     */
    @Getter
    private int code;
    /**
     * 错误消息
     */
    @Getter
    private String message;

    private BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static BusinessException build(int code) {
        return BusinessException.build(code, null);
    }

    public static BusinessException build(int code, String message) {
        return new BusinessException(code, message);
    }

    /**
     * 限流的异常
     * @return
     */
    public static BusinessException frequency() {
        return build(1000, "请求太频繁, 请稍后再试哦");
    }

    /**
     * 限流的异常
     * @return
     */
    public static BusinessException lock() {
        return build(1001, "请求太频繁, 请稍后再试哦");
    }


    public static BusinessException invalidParams() {
        return new BusinessException(0, "请检查参数");
    }

    @Override
    public String toString() {
        return JSON.stringify(this);
    }
}
