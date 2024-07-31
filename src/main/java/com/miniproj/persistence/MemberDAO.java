package com.miniproj.persistence;

public interface MemberDAO {
	// 유저의 userPoint를 수정하는 메서드
	int updateUserPoint(String userId) throws Exception ;
	
	// tmpUserId 아이디 중복여부 조회 메서드
	int selectDuplicateId(String tmpUserId) throws Exception ;
}
