package com.artino.service.desensitization;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizationSerializer.class)
public @interface Desensitization {

    DesensitizationType type() default DesensitizationType.NONE;

    String mask() default "*";

    public static enum DesensitizationType {
        /**
         * 无
         */
        NONE,
        /**
         * 手机号
         */
        PHONE,
        /**
         * 邮箱
         */
        EMAIL,
        /**
         * 身份证
         */
        ID,
        /**
         * 远程文件
         */
        FILE,
        /**
         * 银行卡号
         */
        BANKCARD
    }
}
