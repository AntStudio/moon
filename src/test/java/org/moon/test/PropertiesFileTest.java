package org.moon.test;

import java.net.URL;

/**
 * 配置文件测试
 * @author Gavin
 * @date 2013-8-20 下午11:54:07
 */
public class PropertiesFileTest {

	public static void main(String[] args) {
		URL url = PropertiesFileTest.class.getResource("/asd");
		System.out.println(url);
	}
	
}
