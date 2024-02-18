package com.artino.service.utils;

import java.util.Objects;

public class KeyUtils {
    /**
     * 获取账户登录失败的次数Key
     *
     * @param account 帐户
     * @return key
     */
    public static String getAccountLock(String account) {
        return String.format("lock.%s", account);
    }

    /**
     * 获取缓存用户信息的key
     *
     * @param id 用户id
     * @return key
     */
    public static String getUserKey(Long id) {
        return String.format("id.%d", id);
    }

    /**
     * 获取用户token的key
     *
     * @param token token
     * @return key
     */
    public static String getTokenKey(String token) {
        String Terminal = ServletUtils.currentRequest().getHeader("Terminal");
        return String.format("token.%s.%s", Objects.isNull(Terminal) ? "App" : Terminal, token);
    }
}
