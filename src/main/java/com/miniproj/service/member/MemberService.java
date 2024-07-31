package com.miniproj.service.member;

public interface MemberService {
	// 아이디중복을 확인하는 메서드
	boolean idIsDuplicate(String tmpUserId) throws Exception;
}
