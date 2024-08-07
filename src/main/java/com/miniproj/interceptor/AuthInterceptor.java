package com.miniproj.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproj.util.DestinationPath;


/**
 * @작성자 : 802-05
 * @작성일 : 2024. 8. 7.
 * @프로젝트명 : MiniProject
 * @패키지명 : com.miniproj.interceptor
 * @파일명 : AuthInterceptor.java
 * @클래스명 : AuthInterceptor
 * @description : 
 *  로그인 인증이 필요한 페이지에서 클라이언트가 로그인 했는지 여부 검사 
 로그인이 되어있지 않으면 로그인페이지로 이동, 로그인이 되어있다면 원래 클라이언트의 요청을 수행
 로그인이 되어있지 않아서 로그인페이지로 갔다면 로그인을 성공한 뒤에는 원래 요청을 수행할 수 있도록 해야한다..
 글수정, 글삭제, 답글작성, 답글수정, 답글삭제, 관리자페이지는 로그인이 되어있어야 할 뿐 아니라, 글을 쓴 사람만 할 수있다(글작성자가 클라이언트와 동일한지도 확인)

 */

public class AuthInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * @작성자 : 802-05
	 * @작성일 : 2024. 8. 7.
	 * @프로젝트명 : MiniProject 
	 * @패키지명 : com.miniproj.interceptor
	 * @파일명 : AuthInterceptor.java
	 * @클래스명 : AuthInterceptor
	 * @description : 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("[AuthInterceptor PreHandler 작동중!");
		boolean goOriginPath = false; // 원래 요청한 경로
		
		new DestinationPath().setDestPath(request); // 로그인하기 전에 호출했던 페이지 저장
		
		HttpSession ses = request.getSession();
		if(ses.getAttribute("loginMember")==null) {
			// 로그인X
			System.out.println("[AuthInterceptor : 로그인을 하지 않았습니다! ]");
			response.sendRedirect("/member/login");
		} else {
			// 로그인O
			System.out.println("[AuthInterceptor : 로그인이 되었습니다! ]");
			goOriginPath = true;
		}
		return goOriginPath;
	}
	
}
