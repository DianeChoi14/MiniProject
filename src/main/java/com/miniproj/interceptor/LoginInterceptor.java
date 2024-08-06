package com.miniproj.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproj.model.MemberVO;

// 직접 로그인하는 동작과정을 인터셉트로 구현
// get방식 post방식으로 요청되어 인터셉트가 동작하는 것을 구분
public class LoginInterceptor extends HandlerInterceptorAdapter {

	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("[LoginInterceptor preHandle()호출]");
		// 이미 로그인이 된 경우 로그인 페이지를 보여줄 필요가 없다
		// 로그인되어있지 않은 경우에만 로그인페이지를 보여줌
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		// 포스트방식으로 호출됐을 때만 이 인터셉트가 실행됨
		if (request.getMethod().toUpperCase().equals("POST")) {
			System.out.println("[LoginInterceptor postHandle()호출]");
			Map<String, Object> model = modelAndView.getModel();
			MemberVO loginMember = (MemberVO) model.get("loginMember");
			if (loginMember != null) {
				System.out.println("[LoginInterceptor postHandle() : 로그인 성공]");
				HttpSession ses = request.getSession(); // 로그인요청으로부터 세션을 얻어온다...
				ses.setAttribute("loginMember", loginMember); // 로그인한 유저의 정보를 세션에 저장
				response.sendRedirect("/");
			} else {
				System.out.println("[LoginInterceptor postHandle() : 로그인 실패]");
				response.sendRedirect("/member/login?status=fail");
			} 
		}
			
	}

}
