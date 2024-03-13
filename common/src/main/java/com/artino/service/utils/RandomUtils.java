package com.artino.service.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomUtils {
    /**
     * get uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取指定长度的随机字符串
     *
     * @param length
     * @return
     */
    public static String randomStr(int length, boolean charStart) {
        String pool = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        List<String> list = new ArrayList<>();
        while (list.size() < length) {
            int index = (int) (Math.random() * pool.length());
            list.add(pool.substring(index, 1 + index));
        }
        if (charStart) {
            int index = (int) (Math.random() * 52.0) + 10;
            list.set(0, pool.substring(index, 1 + index));
        }
        return String.join("", list);
    }

    public static String randomStr(int length) {
        return randomStr(length, false);
    }

    /**
     * 获取指定长度的数字随机数
     *
     * @param length
     * @return
     */
    public static int randomNumber(int length) {
        double result = Math.random() * (Math.pow(10, length) - Math.pow(10, length - 1)) + Math.pow(10, length - 1);
        return (int) result;
    }

    public static String getRandomName(int len) {
        if (len <= 0) len = new Random().nextInt(10000) % 6 + 1;
        return "User-" + randomStr(len);
    }

    /**
     * 随机生成中文名
     * @param len
     * @return
     */
    public static String getChineseName(int len) {
        if (len <= 0) len = new Random().nextInt(10000) % 3 + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int hightPos, lowPos;
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39)));
            lowPos = (161 + Math.abs(random.nextInt(93)));
            byte[] b = new byte[2];
            b[0] = Integer.valueOf(hightPos).byteValue();
            b[1] = Integer.valueOf(lowPos).byteValue();
            try {
                sb.append(new String(b, "GBK"));
            } catch (Exception ex) {
                return "游客";
            }
        }
        return sb.toString();
    }
}