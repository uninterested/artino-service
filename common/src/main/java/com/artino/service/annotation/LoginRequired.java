package com.artino.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface LoginRequired {

    /**
     * 类别
     * @return
     */
    UserType type() default UserType.USER;

    enum UserType {
        ADMIN,
        USER
    }
}