package com.miniproj.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.MemberVO;
import com.miniproj.service.hboard.HBoardService;
import com.miniproj.util.DestinationPath;

import lombok.RequiredArgsConstructor;


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

@RequiredArgsConstructor
public class AuthInterceptor extends HandlerInterceptorAdapter {
	
	private final HBoardService service;
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
			System.out.println("[AuthInterceptor : 로그인이 되었습니다! 권한을 확인하세요! ]");
			// 만약 글수정/삭제 페이지의 호출일 경우, 그 글의 수정/삭제권한(본인의 글) > 경로이동 or 에러메시지 및 리다이렉트 
			String uri = request.getRequestURI();
			if(uri.contains("modify") || uri.contains("remove")) {
				int boardNo = Integer.parseInt(request.getParameter("boardNo")); // db에 가져가서 권한 확인 용
				System.out.println(boardNo + "번 글 수정/삭제에 대한 권한을 확인합니다...");
				List<BoardDetailInfo> board = service.read(boardNo);
				// 비교할 로그인 유저 정보 세션에서 불러오기
				MemberVO loginMember = (MemberVO) ses.getAttribute("loginMember"); 
				if(!board.get(0).getWriter().equals(loginMember.getUserId())) {
					System.out.println(boardNo + "번 글 수정/삭제에 대한 권한을 없음! 상세페이지로 이동~");
					response.sendRedirect("/hboard/viewBoard?status=authFail&boardNo=" + boardNo);
				}
			}
			goOriginPath = true;
		}
		return goOriginPath;
	}
	
}
