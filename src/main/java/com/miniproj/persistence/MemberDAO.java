package com.miniproj.persistence;

import com.miniproj.model.AutoLoginInfo;
import com.miniproj.model.LoginDTO;
import com.miniproj.model.MemberVO;
import com.miniproj.model.PointLogDTO;

public interface MemberDAO {
	// 유저의 userPoint를 수정하는 메서드
	int updateUserPoint(PointLogDTO pointLogDTO) throws Exception ;
	
	// tmpUserId 아이디 중복여부 조회 메서드
	int selectDuplicateId(String tmpUserId) throws Exception ;
	
	// 회원 저장 메서드
	int insertMember(MemberVO registMember) throws Exception;
	
	// 로그인 메서드
	MemberVO login(LoginDTO loginDTO) throws Exception;

	// 자동로그인 정보를 저장하는 메서드 
	int updateAutoLoginInfo(AutoLoginInfo autoLoginInfo) throws Exception ;

	// 자동로그인 쿠키 확인
	MemberVO checkAutoLogin(String savedCookieSessionId);
}
