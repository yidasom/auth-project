package com.sample.util;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * com.demo.util
 *
 * @author : idasom
 * @data : 4/28/25
 */
public class SecurityUtil {

    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     *
     * @param data 암호화할 비밀번호
     * @return 암호화된 비밀번호
     * @throws Exception
     */
    public static String encryptPassword(String data) throws Exception {
        if (data == null) {
            return "";
        }
        byte[] plainText = null;
        byte[] hashValue = null;

        MessageDigest md;
        plainText = data.getBytes("UTF-8");
        md = MessageDigest.getInstance("SHA-256");
        hashValue = md.digest(plainText);
        return new String(Base64.getEncoder().encode(hashValue));
    }
}
