package com.miniproj.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 아래의 클래스가 컨트롤러 객체임을 명시
@RequestMapping("/member")
public class MemberController {
	
	@RequestMapping("/register")
	public void showRegisterForm() {
		
	}
}
