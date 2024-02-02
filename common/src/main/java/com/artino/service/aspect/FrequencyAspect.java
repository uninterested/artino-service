package com.artino.service.aspect;


import com.artino.service.annotation.Frequency;
import com.artino.service.base.BusinessException;
import com.artino.service.context.RequestContext;
import com.artino.service.utils.RedisUtils;
import com.artino.service.utils.SpELUtils;
import com.artino.service.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Aspect
@Component
public class FrequencyAspect {
    @Before(value = "@annotation(com.artino.artino.annotation.Frequency)||@annotation(com.artino.artino.annotation.FrequencyGroup)")
    public void doBefore(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Frequency[] frequencies = method.getAnnotationsByType(Frequency.class);
        Map<String, Frequency> keyMap = new HashMap<>();
        for (int i = 0; i < frequencies.length; i++) {
            Frequency frequency = frequencies[i];
            String prefix = StringUtils.isNotEmpty(frequency.alias())
                    ? frequency.alias()
                    : String.format("%s#%d", SpELUtils.getMethodString(method), i);
            String key = switch (frequency.type()) {
                case EL -> SpELUtils.evalSpEL(method, joinPoint.getArgs(), frequency.spEL());
                case FP -> RequestContext.get().getFp();
                case IP -> RequestContext.get().getIp();
                case UID -> Optional.ofNullable(RequestContext.get().getUid()).orElse(0L).toString();
            };
            keyMap.put(String.format("%s:%s", prefix, key), frequency);
        }
        ArrayList<String> keys = new ArrayList<>(keyMap.keySet());
        List<Integer> countList = RedisUtils.mget(keys, Integer.class);
        for (int i = 0; i < keys.size(); i++) {
            Integer count = countList.get(i);
            if (Objects.nonNull(count) && count >= keyMap.get(keys.get(i)).count())
                throw BusinessException.frequency();
        }
        keyMap.forEach((k, v) -> RedisUtils.inc(k, v.time(), v.unit()));
    }
}
