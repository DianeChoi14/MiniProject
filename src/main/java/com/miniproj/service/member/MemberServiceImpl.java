package com.miniproj.service.member;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproj.model.AutoLoginInfo;
import com.miniproj.model.LoginDTO;
import com.miniproj.model.MemberVO;
import com.miniproj.model.PointLogDTO;
import com.miniproj.persistence.MemberDAO;
import com.miniproj.persistence.PointLogDAO;

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
	
	private final PointLogDAO pDao; 
	
	
	@Override
	public boolean idIsDuplicate(String tmpUserId) throws Exception {
		System.out.println(mDao + "DAO객체 잘 나와요?");
		boolean result= false;
		if(mDao.selectDuplicateId(tmpUserId) == 1) {
			result=true; // 중복될 경우
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public boolean saveMember(MemberVO registMember) throws Exception {
		// 1:N관계인 취미컬럼을 하나의 문자열로 저장
		boolean result = false;
		String tmpHobbies="";
		for(String hobby : registMember.getHobby())
		{
			tmpHobbies +=  hobby + "," ;
		}
		registMember.setHobbies(tmpHobbies);
		
		// 1) 회원데이터 db에 저장
		if(mDao.insertMember(registMember) == 1) {
			// 2) 회원가입한 멤버에 100포인트 부여
			if(pDao.insertPointLog(new PointLogDTO(registMember.getUserId(), "회원가입"))==1) {
				result = true;
			}
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public MemberVO login(LoginDTO loginDTO) throws Exception {
		
		// 1) 로그인시도 성공? select
		MemberVO loginMember = mDao.login(loginDTO);
		if(loginMember != null) {
			// 2) 로그인 성공 시 pointLog에 1포인트 부여 (insert)
			PointLogDTO pointLogDTO = new PointLogDTO(loginDTO.getUserId(), "로그인");
			if(pDao.insertPointLog(pointLogDTO)==1) {
				// 3) Member 포인트 update
				mDao.updateUserPoint(pointLogDTO);
			};
		}
		return loginMember;
	}

	@Override
	public boolean saveAutoLoginInfo(AutoLoginInfo autoLoginInfo) throws Exception {
		boolean result = false;
		if(mDao.updateAutoLoginInfo(autoLoginInfo) == 1) {
			result = true;
		}
		return result;
	}

}
