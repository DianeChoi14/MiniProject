package com.miniproj.service.member;

import com.miniproj.model.AutoLoginInfo;
import com.miniproj.model.LoginDTO;
import com.miniproj.model.MemberVO;

public interface MemberService {
	// 아이디중복을 확인하는 메서드
	boolean idIsDuplicate(String tmpUserId) throws Exception;

	// DB에 회원데이터를 저장하는 메서드
	boolean saveMember(MemberVO registMember) throws Exception;

	// 로그인을 시키는 메서드
	MemberVO login(LoginDTO loginDTO) throws Exception;
	
	// 자동로그인 정보를 저장하는 메서드
	boolean saveAutoLoginInfo(AutoLoginInfo autoLoginInfo) throws Exception;

	// 자동로그인 쿠키 확인
	MemberVO checkAutoLogin(String savedCookieSessionId)  throws Exception;
	
}
