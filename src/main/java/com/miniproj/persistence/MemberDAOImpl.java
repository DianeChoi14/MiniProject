package com.miniproj.persistence;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.miniproj.model.AutoLoginInfo;
import com.miniproj.model.LoginDTO;
import com.miniproj.model.MemberVO;
import com.miniproj.model.PointLogDTO;

@Repository
public class MemberDAOImpl implements MemberDAO {

	@Autowired
	private SqlSession ses;
	
	private static String NS = "com.miniproj.mappers.membermapper";
	// static은 클래스가 로딩될 때 모든 메소드들이 공유한다, static이 없으면 객체가 만들어 져야 메모리에 올라감
	
	@Override
	public int updateUserPoint(PointLogDTO pointLogDTO) throws Exception {
		
		return ses.update(NS + ".updateUserPoint", pointLogDTO);
	}

	@Override
	public int selectDuplicateId(String tmpUserId) throws Exception {
		
		return ses.selectOne(NS + ".selectUserId", tmpUserId);
	}

	@Override
	public int insertMember(MemberVO registMember) throws Exception {
		
		return ses.insert(NS + ".insertMember", registMember);
	}

	@Override
	public MemberVO login(LoginDTO loginDTO) throws Exception {

		return ses.selectOne(NS + ".loginWithLoginDTO", loginDTO);
	}

	@Override
	public int updateAutoLoginInfo(AutoLoginInfo autoLoginInfo) throws Exception {

		return ses.update(NS + ".updateAutoLoginInfo", autoLoginInfo);
	}

	@Override
	public MemberVO checkAutoLogin(String savedCookieSessionId) {
		
		return ses.selectOne(NS + ".checkAutoLoginUser", savedCookieSessionId);
	}

	
}

