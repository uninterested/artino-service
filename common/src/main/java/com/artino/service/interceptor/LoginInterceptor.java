package com.artino.service.interceptor;

import com.artino.service.annotation.LoginRequired;
import com.artino.service.base.BusinessException;
import com.artino.service.context.RequestContext;
import com.artino.service.context.RequestInfo;
import com.artino.service.utils.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
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
            LoginRequired anno = ((HandlerMethod) handler).getMethod().getAnnotation(LoginRequired.class);
            if (Objects.isNull(anno)) {
                handleNoneRequest(request);
                return true;
            }
            handleRequest(request, anno);
            Long userId = RequestContext.get().getUid();
            if (null == userId || userId == 0) throw BusinessException.build(403, "请先登陆账号");
            return true;
        } else {
            handleNoneRequest(request);
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContext.remove();
    }

    /**
     * 处理请求
     * @param request 请求
     * @param anno 注解
     */
    private void handleRequest(HttpServletRequest request, LoginRequired anno) {
        Environment env = SpringUtils.getBean(Environment.class);
        String tokenName = env.getProperty("constant.login.token", "X-Token");
        String token = request.getHeader(tokenName);
        Long userId;
        if (StringUtils.isEmpty(token)) userId = null;
        else {
            String idStr = anno.type() == LoginRequired.UserType.ADMIN
                    ? RedisUtils.get(KeyUtils.getTokenKey(token), String.class)
                    : RedisUtils.get(KeyUtils.getUserTokenKey(token), String.class);
            if (StringUtils.isEmpty(idStr)) userId = null;
            else userId = Long.parseLong(idStr);
        }
        String ip = IpUtils.getHostIp();
        String fp = StringUtils.isEmpty(request.getHeader("fp")) ? "" : request.getHeader("fp");
        RequestContext.set(new RequestInfo(userId, ip, fp));
    }

    private void handleNoneRequest(HttpServletRequest request) {
        String ip = IpUtils.getHostIp();
        String fp = StringUtils.isEmpty(request.getHeader("fp")) ? "" : request.getHeader("fp");
        RequestContext.set(new RequestInfo(null, ip, fp));
    }
}