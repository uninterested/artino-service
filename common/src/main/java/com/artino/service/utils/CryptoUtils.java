package com.artino.service.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
public class CryptoUtils {
    /**
     * 获取token
     *
     * @return
     */
    public static String createToken() {
        String token = String.format("%s_%s", UUID.randomUUID(), DateUtils.dateTimeNow());
        return md5Encode(base64Encode(token));
    }

    /**
     * create random
     * @return
     */
    public static String randomKey() {
        String token = String.format("%s_%s", UUID.randomUUID(), DateUtils.dateTimeNow());
        return md5Encode(base64Encode(token));
    }

    /**
     * base64加密
     *
     * @param input
     * @return
     */
    public static String base64Encode(final String input) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodeData = encoder.encode(input.getBytes());
        return new String(encodeData);
    }

    public static String base64Encode(final byte[] input) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodeData = encoder.encode(input);
        return new String(encodeData);
    }

    /**
     * base64解密
     *
     * @param input
     * @return
     */
    public static String base64Decode(final String input) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodeData = decoder.decode(input);
        return new String(decodeData);
    }

    /**
     * md5加密
     * @param input
     * @return
     */
    public static String md5Encode(final String input) {
        return md5Encode(input, false);
    }

    /**
     * des 加密
     * @param input
     * @param secret
     * @return
     */
    public static String desEncode(final String input, final String secret) {
        try {
            String md5 = md5Encode(secret).toLowerCase();
            DESKeySpec desKeySpec = new DESKeySpec(md5.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * des 解密
     * @param input
     * @param secret
     * @return
     */
    public static String desDecode(final String input, final String secret) {
        try{
            String md5 = md5Encode(secret).toLowerCase();
            DESKeySpec desKeySpec = new DESKeySpec(md5.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decode = Base64.getDecoder().decode(input);
            byte[] decipherByte = cipher.doFinal(decode);
            return new String(decipherByte);
        }catch (Exception e) {
            return null;
        }
    }

    public static String md5Encode(final String input, final boolean origin) {
        return md5Encode(input.getBytes(), origin);
    }

    public static String md5Encode(final byte[] bytes) {
        return md5Encode(bytes, false);
    }

    public static String md5Encode(final byte[] bytes, final boolean origin) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            //
        }
        StringBuilder md5code = null;
        if (secretBytes != null) {
            md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        }
        if (md5code != null) {
            for (int i = 0; i < 32 - md5code.length(); i++) md5code.insert(0, "0");
        }
        if (origin) if (md5code != null) {
            return md5code.toString();
        }
        if (md5code != null) {
            return md5code.toString().toLowerCase();
        }
        return null;
    }


    /**
     * 加密用户的密码
     * @param password 用户密码
     * @param id 用户id
     * @return
     */
    public static String encryptPassword(final String password, final Long id) {
        String pwd = String.format("%s%d", password, id);
        return md5Encode(pwd);
    }
}
