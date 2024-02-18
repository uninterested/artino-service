package com.artino.service.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> {
    private final int code;
    private final String message;
    private final T data;

    public static<T> R<T> success() {
        return R.success(null);
    }

    public static<T> R<T> success(T data) {
        return new R<T>(200, null, data);
    }

    public static<T> R<T> error(int code, String message) {
        return new R<>(code, message, null);
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
