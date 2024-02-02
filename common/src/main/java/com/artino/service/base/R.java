package com.artino.service.base;

import lombok.Getter;

public class R<T> {
    @Getter
    private int code;
    @Getter
    private String message;
    @Getter
    private T data;

    public static R<Object> success() {
        return R.success(null);
    }

    public static R<Object> success(Object data) {
        return new R<Object>(200, null, data);
    }

    public static R error(int code, String message) {
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
}
