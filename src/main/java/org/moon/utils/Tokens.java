package org.moon.utils;

/**
 * 令牌工具类
 * @author:Gavin
 * @date 2015/4/22 0022
 */
public class Tokens {

    /**
     * 根据名字获取令牌字符串
     * @param phoneNumber
     * @param password
     * @return
     */
    public static String getTokenString(String phoneNumber,String password){
        return new String(SHA1.getSHA1("u:" + phoneNumber + "p:" + password + "xheart_" + System.currentTimeMillis()));
    }

}
