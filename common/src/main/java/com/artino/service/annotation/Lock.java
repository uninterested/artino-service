package com.artino.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {
    /** lock 别名 */
    String alias() default "";

    /** el表达式 */
    String spEl();

    /** 等待锁的时间，默认-1，不等待直接失败,redisson默认也是-1 */
    int waitTime() default -1;

    /** 等待锁的时间单位，默认毫秒 */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}