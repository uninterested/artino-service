package com.artino.service.utils;

public class KeyUtils {
    /**
     * 获取账户登录失败的次数Key
     * @param account
     * @return
     */
    public static String getAccountLock(String account) {
        return String.format("lock.%s", account);
    }

    /**
     * 获取缓存用户信息的key
     * @param id
     * @return
     */
    public static String getUserKey(long id) {
        return String.format("id.%d", id);
    }

    /**
     * 获取用户token的key
     * @param token
     * @return
     */
    public static String getTokenKey(String token) {
        return String.format("token.%s", token);
    }
}
