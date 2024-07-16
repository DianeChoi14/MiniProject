package com.miniproj.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Calendar;
//import java.lang 생략가능

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.miniproj.model.BoardUpFilesVODTO;

@Component // 스프링이 컨테이너에게 객체를 만들어 관리하도록하는 어노테이션
public class FileProcess {
	
	// file을 realPath에 저장하는 메서드
	public BoardUpFilesVODTO saveFileToRealPath(byte[] upfile, String realPath, String contentType, String originalFileName, long fileSize) throws IOException {
		
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
		
		String saveFilePath = realPath + ymd[ymd.length-1]; // 실제 파일의 저장경로
		String newFileName = null;
		String ext = originalFileName.substring(originalFileName.lastIndexOf(".")+1);
		if(fileSize>0) {
			// 파일이름 중복 검사 후, 중복될 경우 이름변경
			if(checkFileExist(saveFilePath, originalFileName)) {
				newFileName = renameFileName(originalFileName);
			} else {
				newFileName = originalFileName;
			}
			
			if(ImageMimeType.isImage(ext)) {
				// 이미지파일파일 > 썸네일이미지, base64문자열 만들고 이미지와 함께 저장
			} else {
				// 이미지파일X > 그냥 현재 파일만 하드디스크에 저장
				File saveFile = new File(saveFilePath + File.separator + newFileName);
				FileUtils.writeByteArrayToFile(saveFile, upfile); // file 파일이 저장될 경로와 객체, 실제 파일 저장
				
				result = BoardUpFilesVODTO.builder()
						.ext(contentType)
						.newFileName(newFileName)
						.originalFileName(originalFileName)
						.size(fileSize)
						.build();
			}
		}
		
		return result; // 저장된 파일의 정보를 담은 객체
		
	}
	
	// 파일이름을 바꾸는 메서드
	// ex. originalFileName_timeStamp.확장자
	private String renameFileName(String originalFileName) {
		String timeStamp = System.currentTimeMillis()+""; // 현재시각을 타임스탬프값으로 리턴
		String ext = originalFileName.substring(originalFileName.lastIndexOf(".")+1);
		String fileNameWithOutExt = originalFileName.substring(0,originalFileName.lastIndexOf("."));
		String newFileName = fileNameWithOutExt + "_" + timeStamp + "." + ext ;
//		System.out.println("old filename = " + originalFileName);
//		System.out.println("새로운 파일 이름 : " + newFileName);
		
		return newFileName;
	}

	// originalFileName이 saveFilePath에 존재하는지 (파일 중복여부 확인)
	// 중복된 파일이 있다면 true 없으면 false
	private boolean checkFileExist(String saveFilePath, String originalFileName) {
		File tmp = new File(saveFilePath);
		boolean isFind = false;
		
		for(String name : tmp.list()) {
			if(name.equals(originalFileName)) {
				System.out.println("같은 이름이 있다");
				isFind = true;
			}
		}
		if(!isFind) {
			System.out.println("같은이름의 파일이 없는 상태");
		}
		return isFind;
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
