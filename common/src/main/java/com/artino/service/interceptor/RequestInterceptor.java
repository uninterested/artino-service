package com.artino.service.interceptor;

import com.artino.service.context.RequestContext;
import com.artino.service.context.RequestInfo;
import com.artino.service.utils.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        Environment env = SpringUtils.getBean(Environment.class);
        String tokenName = env.getProperty("constant.login.token", "X-Token");
        String token = request.getHeader(tokenName);
        Long userId;
        if (StringUtils.isEmpty(token)) userId = null;
        else {
            String idStr = RedisUtils.get(KeyUtils.getTokenKey(token), String.class);
            if (StringUtils.isEmpty(idStr)) userId = null;
            else userId = Long.parseLong(idStr);
        }
        String ip = IpUtils.getHostIp();
        String fp = StringUtils.isEmpty(request.getHeader("fp")) ? "" : request.getHeader("fp");
        RequestInfo info = new RequestInfo(userId, ip, fp);
        RequestContext.set(info);
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        RequestContext.remove();
    }
}