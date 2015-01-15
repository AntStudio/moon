package org.moon.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类.
 * MD5加密后为128位数字,由16个字节,32个16进制数字组成.
 * @author Gavin
 * @date 2014年2月21日
 */
public class MD5 {

    public static String getMD5(String src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
                    'd', 'e', 'f' };
            md.update(src.getBytes());
            char[] ss = new char[32];
            int current = 0;
            for (byte b : md.digest()) {
                ss[current++] = hexDigits[b >>> 4 & 0x0F];
                ss[current++] =hexDigits[b & 0x0F];
            }
            return new String(ss);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCryptographicPassword(String password){
        return getMD5(password+"Moon");
    }
}
