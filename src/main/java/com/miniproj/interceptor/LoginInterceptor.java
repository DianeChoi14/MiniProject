package com.miniproj.interceptor;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproj.model.MemberVO;
import com.mysql.cj.util.StringUtils;

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
				
				// request에서 자동로그인 체크 여부 확인(로그인 성공한 사람) > 쿠키에 저장
				if(request.getParameter("remember")!=null) { // getParameter : on/null
					saveAutoLoginInfo(request, response); // 자동로그인 정보 저장 메서드
				}
				
				Object tmp = ses.getAttribute("destPath");
				response.sendRedirect((tmp==null)? "/" : (String)tmp);
			} else {
				System.out.println("[LoginInterceptor postHandle() : 로그인 실패]");
				response.sendRedirect("/member/login?status=fail");
			} 
		}
			
	}

	private void saveAutoLoginInfo(HttpServletRequest request,HttpServletResponse response) {
		// 쿠키만들 때는 name, value, Expires 
		// Expires : Session 세션이 유지될 때까지 유효
		Cookie autoLoginCookie = new Cookie("al", request.getSession().getId());
		autoLoginCookie.setMaxAge(60*60*24*7); // 일주일동안 쿠키 유지(자동로그인기간)
		autoLoginCookie.setPath("/"); // 쿠키가 다른 경로에서도 유효하도록 설정
		response.addCookie(autoLoginCookie); // response객체에 쿠키 저장
		
		
	}

}
