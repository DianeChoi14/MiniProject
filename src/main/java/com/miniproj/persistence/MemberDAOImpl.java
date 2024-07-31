package com.miniproj.persistence;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDAOImpl implements MemberDAO {

	@Autowired
	private SqlSession ses;
	
	private static String NS = "com.miniproj.mappers.membermapper";
	// static은 클래스가 로딩될 때 모든 메소드들이 공유한다, static이 없으면 객체가 만들어 져야 메모리에 올라감
	
	@Override
	public int updateUserPoint(String userId) throws Exception {
		
		return ses.update(NS + ".updateUserPoint", userId);
	}

	@Override
	public int selectDuplicateId(String tmpUserId) throws Exception {
		
		return ses.selectOne(NS + ".selectUserId", tmpUserId);
	}

	
}

