package org.moon.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类.
 * @author Gavin
 * @date 2014年2月21日
 */
public class MD5 {

    static byte hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
            'd', 'e', 'f' };

    public static byte[] getMD5(String src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");

            md.update(src.getBytes());
            byte[] ss = new byte[32];
            int current = 0;
            for (byte b : md.digest()) {
                ss[current++] = hexDigits[b >>> 4 & 0x0F];
                ss[current++] =hexDigits[b & 0x0F];
            }
            return ss;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static String getMD5AsString(String src) {
        return new String(getMD5(src));
    }

    public static String getCryptographicPassword(String password){
        return getMD5AsString(password + "Moon");
    }
}
