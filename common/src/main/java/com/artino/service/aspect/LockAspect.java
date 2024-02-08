package com.artino.service.aspect;

import com.artino.service.annotation.Lock;
import com.artino.service.base.BusinessException;
import com.artino.service.utils.SpELUtils;
import com.artino.service.utils.SpringUtils;
import com.artino.service.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@Order(0)
public class LockAspect {
    @Around("@annotation(com.artino.service.annotation.Lock)")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Lock lock = method.getAnnotation(Lock.class);
        String prefix = StringUtils.isNotEmpty(lock.alias()) ? lock.alias() : SpELUtils.getMethodString(method);
        String elKey = SpELUtils.evalSpEL(method, joinPoint.getArgs(), lock.spEl());
        String lockKey = String.format("%s#%s", prefix, elKey);
        return executeWithLock(lockKey, lock.waitTime(), lock.unit(), joinPoint::proceed);
    }

    private <T> T executeWithLock(String key, int waitTime, TimeUnit unit, SupplierThrow<T> supplier) throws Throwable {
        RedissonClient redissonClient = SpringUtils.getBean(RedissonClient.class);
        RLock lock = redissonClient.getLock(key);
        boolean lockSuccess = lock.tryLock(waitTime, unit);
        if (!lockSuccess) throw BusinessException.lock();
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }

    @FunctionalInterface
    interface SupplierThrow<T> {
        T get() throws Throwable;
    }
}
