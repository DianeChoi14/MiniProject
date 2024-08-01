package com.miniproj.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.miniproj.model.MemberVO;
import com.miniproj.model.MyResponseWithoutData;
import com.miniproj.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@Controller // 아래의 클래스가 컨트롤러 객체임을 명시
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
//	@Autowired
	private final MemberService mService;
	
	@RequestMapping("/register")
	public void showRegisterForm() {
		
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public void registerMember(MemberVO registMember, @RequestParam("userProfile") MultipartFile userProfile) {
		System.out.println("회원가입 진행!"+registMember.toString());
		System.out.println("프로필사진 파일명!"+userProfile.getOriginalFilename());
	}
	
	@RequestMapping(value="/isDuplicate", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	 public ResponseEntity<MyResponseWithoutData> isIdDuplicate(@RequestParam("tmpUserId")String tmpUserId) {
		System.out.println(tmpUserId + "중복 확인~~~");
		MyResponseWithoutData json = null;
		ResponseEntity<MyResponseWithoutData> result = null;
		try {
			if(mService.idIsDuplicate(tmpUserId)) {
				// 아이디가 중복된다
				json= new MyResponseWithoutData(200, tmpUserId, "duplicate");
				System.out.println("중복됨");
			} else {
				// 아이디가 중복되지 않는다
				json= new MyResponseWithoutData(200, tmpUserId, "not duplicate");
			}
			result = new ResponseEntity<MyResponseWithoutData>(json, HttpStatus.OK);
		} catch (Exception e) {
			// DB중복여부 확인X
			e.printStackTrace();
			result = new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		return result;
	}
	
}
