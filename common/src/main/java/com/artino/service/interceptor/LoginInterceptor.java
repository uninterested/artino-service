package com.artino.service.interceptor;

import com.artino.service.annotation.LoginRequired;
import com.artino.service.base.BusinessException;
import com.artino.service.context.RequestContext;
import com.artino.service.context.RequestInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.Objects;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws BusinessException {
        if (handler instanceof HandlerMethod) {
            Annotation anno = ((HandlerMethod) handler).getMethod().getAnnotation(LoginRequired.class);
            if (Objects.isNull(anno)) return true;
            RequestInfo info = RequestContext.get();
            Long userId = info.getUid();
            if (null == userId || userId == 0) throw BusinessException.build(403, "请先登陆账号");
            return true;
        }
        return true;
    }
}