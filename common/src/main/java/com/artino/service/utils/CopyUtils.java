package com.artino.service.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;

public class CopyUtils {
    public static <T> T copy(Object source, Class<T> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            T target = clazz.cast(constructor.newInstance());
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            return null;
        }
    }
}
