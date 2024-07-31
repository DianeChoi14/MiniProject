package com.miniproj.service.member;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniproj.persistence.MemberDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final한 멤버mDao에게 생성자를 통해서 필요한 객체를 주입하는 방식(멤버객체가 final일 때만)
public class MemberServiceImpl implements MemberService {
	
//	@Autowired //같은 타입을 찾아서 자동 주입
//	private MemberDAO mDao;
	private final MemberDAO mDao;
	
	// 세터를 선언해서 주입
//	public void setMemberDAO(MemberDAO mdao) {
//		this.mDao = mdao;
//	}
	
	
	@Override
	public boolean idIsDuplicate(String tmpUserId) throws Exception {
		System.out.println(mDao + "DAO객체 잘 나와요?");
		boolean result= false;
		if(mDao.selectDuplicateId(tmpUserId) == 1) {
			result=true; // 중복될 경우
		}
		return result;
	}

}
