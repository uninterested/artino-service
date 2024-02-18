package com.artino.service.utils;

import java.util.List;

public class MessageUtils {
    public static void sendMessage(List<String> accounts, String templete, String code) {
        System.out.println("send to: " + templete.formatted(code));
    }

    public static void sendMessage(List<String> accounts, String templete, int code) {
        sendMessage(accounts, templete, String.valueOf(code));
    }
    public static void sendMessage(String account, String templete, String code) {
        sendMessage(List.of(account), templete, code);
    }

    public static void sendMessage(String account, String templete, int code) {
        sendMessage(List.of(account), templete, code);
    }
}
