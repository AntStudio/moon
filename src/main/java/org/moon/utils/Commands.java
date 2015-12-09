package org.moon.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * 命令行工具类
 * @author:Gavin
 * @date 2015/5/13 0013
 */
public class Commands {

    private static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");

    /**
     * 判断是否是windows系统
     * @return
     */
    public static boolean isWindows(){
        return isWindows;
    }

    /**
     * 执行一条或多条命令
     * @param commands
     * @return
     * @throws IOException
     */
    public static ProcessBuilder exec(String...commands) throws IOException {
        StringBuilder commandBuilder = new StringBuilder();

        StringJoiner sj = new StringJoiner("&&");
        Arrays.stream(commands).forEach(command->sj.add(command));
        commandBuilder.append(sj.toString());

        ProcessBuilder processBuilder ;
        if(isWindows){
            processBuilder = new ProcessBuilder("cmd","/c",commandBuilder.toString());
        }else{
            processBuilder = new ProcessBuilder("sh","-c",commandBuilder.toString());
        }

        return processBuilder;
    }

}
