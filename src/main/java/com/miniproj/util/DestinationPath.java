package com.miniproj.util;

import javax.servlet.http.HttpServletRequest;

// import com.mysql.cj.util.StringUtils;

/**
 * @작성자 : 802-05
 * @작성일 : 2024. 8. 7.
 * @프로젝트명 : MiniProject
 * @패키지명 : com.miniproj.util
 * @파일명 : DestinationPath.java
 * @클래스명 : DestinationPath
 * @description : 로그인을 하지 않은 상태에서 로그인페이지 이동하기 전에 원래 가려던 페이지경로를 저장하는 객체
 */

public class DestinationPath {
	// uri, 쿼리스트링
	private String destPath; // 이 변수를 static으로 만들면 웹에 접속한 클라이언트들의 변수가 공유된다...
	

	/**
	 * @작성자 : 802-05
	 * @작성일 : 2024. 8. 7.
	 * @method_name : setDestPath
	 * @param : HttpServletRequest req
	 * @return : void 
	 * @description : Request객체에서 uri와 queryString을 얻어 목적지 경로(this.destPath)에 할당
	 * Session객체에 바인딩
	*/
	public void setDestPath(HttpServletRequest req) {
		// 글작성 /hboard/saveBoard 
		// 글수정/삭제 : /hboard/modifyBoard?boardNo=1234 (쿼리스트링O
		String uri = req.getRequestURI();
		String queryString = req.getQueryString();
//		if(StringUtils.isNullOrEmpty(queryString)) {
//			// 쿼리스트링이 없을 때
//			this.destPath = uri;
//		} else {
//			// 쿼리스트링이 있을 때
//			this.destPath = uri + "?" + queryString;
//		}
		destPath = (queryString == null)? uri : uri + "?" + queryString;
		req.getSession().setAttribute("destPath", this.destPath);
	}
	
	public void getDestPath() {
		
	}
}
