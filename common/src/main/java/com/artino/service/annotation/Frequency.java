package com.artino.service.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Repeatable(FrequencyGroup.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Frequency {
    /** 频控别名 */
    String alias() default "";

    /** 频控类别 */
    Type type() default Type.IP;

    /** EL表达式，如果type为EL的时候，必须要传值 */
    String spEL() default "";

    /** 频控时间范围 */
    int time();

    /** 频控时间单位 */
    TimeUnit unit() default TimeUnit.SECONDS;

    /** 频控时间内的限制次数 */
    int count();

    enum Type {
        /** IP */
        IP,
        /** 用户ID */
        UID,
        /** EL */
        EL,
        /** 指纹 */
        FP
    }
}
