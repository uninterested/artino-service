package com.artino.service.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    /**
     * 是不是手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        String HTML_PATTERN = "^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1589]))\\d{8}$";
        Pattern pattern = Pattern.compile(HTML_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 是不是email
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email) return false;
        String HTML_PATTERN = "^[a-z0-9A-Z]+([-|_][a-z0-9A-Z]+)*@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(HTML_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 是不是url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        String HTML_PATTERN = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$";
        Pattern pattern = Pattern.compile(HTML_PATTERN);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static void matchVarParams(String input, List<String> list) {
        String HTML_PATTERN = "\\{\\{(.*?)}}";
        Pattern pattern = Pattern.compile(HTML_PATTERN);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String str = matcher.group(1).replace("{{", "").replace("}}", "");
            if (str.startsWith("vars.")) {
                String variable = str.substring(5);
                if (!list.contains(variable)) list.add(variable);
            }
        }
    }
}