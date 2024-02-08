package com.artino.service.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> {
    private final int code;
    private final String message;
    private final T data;

    public static R<Object> success() {
        return R.success(null);
    }

    public static R<Object> success(Object data) {
        return new R<Object>(200, null, data);
    }

    public static R<Object> error(int code, String message) {
        return new R(code, message, null);
    }

    private R(int code) {
        this(code, null, null);
    }

    private R(int code, String message) {
        this(code, message, null);
    }

    private R(int code, T data) {
        this(code, null, data);
    }
    private R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static interface IBaseEnum {
        Integer getSelfValue();
    }
}
