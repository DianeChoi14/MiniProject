package com.miniproj.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproj.message.MessageService;
import com.miniproj.model.MemberVO;

public class MessageConfirmInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private MessageService msgService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		boolean result = true;
		// 로그인을 했을 때 본인에게 온 메시지의 갯수를 가져온다
		HttpSession ses = request.getSession();
		
		if(ses.getAttribute("loginMember") != null) {
			//로그인 했다면
			MemberVO loginMember = (MemberVO) ses.getAttribute("loginMember");
			int unReadMsgCnt = msgService.getMessageCount(loginMember.getUserId());
			
			System.out.println(loginMember.getUserId() + "가 읽지않은 메시지 갯수를 가져올게 : " + unReadMsgCnt);
			
			if(unReadMsgCnt > 0) {
				ses.setAttribute("unReadMsgCnt", unReadMsgCnt); //  세션영역에 저장
			}
			
		}
		return result;
	}

}
