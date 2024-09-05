package com.miniproj.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;

/**
 * @작성자 : 802-05
 * @작성일 : 2024. 9. 5.
 * @프로젝트명 : MiniProject
 * @패키지명 : com.miniproj.util
 * @파일명 : PropertiestTask.java
 * @클래스명 : PropertiestTask
 * @description : classpath:dbconfig.properties파일에서 넘겨받은 key의 value를 반환하도록 한다
 */
public class PropertiestTask {
	public static String getPropertiesValue(String key) throws IOException {
		String resources = "dbconfig.properties";
		Properties prop = new Properties(); // 비어있는 properties 객체생성
		
		Reader reader = Resources.getResourceAsReader(null, resources);
		prop.load(reader); // reader가 가리키는 파일을 읽어서 prop객체에 key와 value를 구분해서 넣어줌
		
		return (String)prop.get(key);
	}
}
