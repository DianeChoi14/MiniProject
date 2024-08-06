package com.miniproj.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// Interceptor은 컨트롤러에 들어오는 요청 HttpServletRequest와 컨트롤러가 응답하는 HttpResponse를 가로채는 역할

public class InterceptorExample extends HandlerInterceptorAdapter {

	// preHandle : mapping되는 컨트롤러단의 메서드가 호출되기 전에 request와 response를 배앗아와서 동작
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("인터셉터 preHnadle : boolean반환값에 따라 컨트롤러단의 해당 메서드로 제어가 돌아간다/가지 않는다");
		return super.preHandle(request, response, handler); // 부모가 가지고 있는 결과값을 호출해서 반환
//		return true; // 반환값에 따라 컨트롤러단의 해당 메서드로 제어가 돌아간다
//		return false; // 반환값에 따라 컨트롤러단의 해당 메서드로 제어가 돌아가지 않는다
	}

	// postHandle : mapping되는 컨트롤러단의 메서드가 호출되어 실행된 후에 request와 response를 빼앗아와서 동작
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("인터셉터 postHnadle==========================");
		super.postHandle(request, response, handler, modelAndView);
	}

	// 해당 interceptor의 preHandle, postHandle의 전 과정이 끝난 후에 request와 response를 빼앗아와서 동작
	// view단이 렌더링 된 후에.. 컨트롤러단에서 예외처리를 해야하는 경우, 인터셉터와 맞물린 컨트롤러단의 경우 예외처리를 한번에 진행
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("인터셉터 afterCompletion----------------------------");
		super.afterCompletion(request, response, handler, ex);
	}
	
}
