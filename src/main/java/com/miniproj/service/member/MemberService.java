package com.miniproj.service.member;

import com.miniproj.model.MemberVO;

public interface MemberService {
	// 아이디중복을 확인하는 메서드
	boolean idIsDuplicate(String tmpUserId) throws Exception;

	// DB에 회원데이터를 저장하는 메서드
	boolean saveMember(MemberVO registMember) throws Exception;
}
