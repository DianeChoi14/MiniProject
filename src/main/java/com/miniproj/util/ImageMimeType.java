package com.miniproj.util;

import java.util.HashMap;
import java.util.Map;

// 이미지인지 아닌지 검증하는 클래스
public class ImageMimeType {

	private static Map<String, String> imageMimeType;
	
	// instance멤버를 초기화하는 초기화블럭
	{
		
	}
	// static멤버를 초기화하는 블럭
	static {
		imageMimeType = new HashMap<String, String>();
		
		imageMimeType.put("jpg", "image/jpeg");
		imageMimeType.put("jpeg", "image/jpeg");
		imageMimeType.put("gif", "image/gif");
		imageMimeType.put("png", "image/jpng");

	}
	
	// ext가 이미지인지(true) 아닌지(false) 
	public static boolean isImage(String ext) {
		return imageMimeType.containsKey(ext);
	}

}
