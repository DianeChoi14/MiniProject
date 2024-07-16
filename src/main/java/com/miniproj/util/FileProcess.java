package com.miniproj.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.miniproj.model.BoardUpFilesVODTO;

@Component // 스프링이 컨테이너에게 객체를 만들어 관리하도록하는 어노테이션
public class FileProcess {
	
	// file을 realPath에 저장하는 메서드
	public BoardUpFilesVODTO saveFileToRealPath(byte[] upfile, String realPath, String ext, String originalFileName, long fileSize) {
		
		BoardUpFilesVODTO result = null;
//		
//		String ext = file.getContentType(); // mimetype
//		String originalFileName = file.getOriginalFilename();
//		long fileSize = file.getSize();
//		
//		byte[] upFile = file.getBytes(); // 저장될 타입의 실제 content
		
		// 파일이 실제 저장되는 경로 realPath + "/연도/월/일" 경로
		String[] ymd = makeCalendarPath(realPath);
		makeDirectory(realPath, ymd);
		
		return result;
		
	}
	// 파일이 저장될 경로의 디렉토리 구조를 "/연/월/일"형태로 만드는 메서드
	private String[] makeCalendarPath(String realPath) {
		Calendar now = Calendar.getInstance(); // 현재날짜및시간 객체
		// final static : 상수, 값을 변경할 수 없음
		// .separator 파일구분자를 얻어다 써줌
		String year = File.separator + now.get(Calendar.YEAR) + ""; // '\2024'
		String month = year + File.separator + new DecimalFormat("00").format(now.get(Calendar.MONTH)+1); // '\2024\07'
		String date = month + File.separator + new DecimalFormat("00").format(now.get(Calendar.DATE)); // '2024\07\16'
		System.out.println(year + ',' + month + ',' + date);
		
		String[] ymd = {year, month, date};
		
		return ymd;
	}
	//실제 디렉토리를 만드는 메서드
	// 가변인자 메서드(eg String...ymd) : 전달된 year, month, date의 값이 ymd라는 하나의 배열로 처리한다
	private void makeDirectory(String realPath, String...ymd) {
		// 디렉토리가 존재하지 않을 경우 폴더를 만드는 메서드
		if(!new File(realPath+ymd[ymd.length-1]).exists()) {
			for(String path : ymd) {
				File tmp = new File(realPath + path);
				// realPath 아래 '\2024' > '\2024\07' > '\2024\07\16' 폴더존재여부 확인하고 폴더 생성
				if(!tmp.exists()) {
					tmp.mkdir();
				}
			}
		}
	}
}
