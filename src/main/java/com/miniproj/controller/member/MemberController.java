package com.miniproj.controller.member;

import java.io.IOException;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.miniproj.model.LoginDTO;
import com.miniproj.model.MemberVO;
import com.miniproj.model.MyResponseWithoutData;
import com.miniproj.service.member.MemberService;
import com.miniproj.util.FileProcess;
import com.miniproj.util.SendMailService;
import com.mysql.cj.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Controller // 아래의 클래스가 컨트롤러 객체임을 명시
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
//	@Autowired
	private final MemberService mService;
	private final FileProcess fp;
	
	@RequestMapping("/register")
	public void showRegisterForm() {
		
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerMember(MemberVO registMember, @RequestParam("userProfile") 
	MultipartFile userProfile, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		
		String resultPage = "redirect:/";
		// 프로필 파일이름을 '유저아이디.확장자'로 바꾸기
			System.out.println("프로필사진 파일명!"+userProfile.getOriginalFilename());
		String tmpUserProfileName = userProfile.getOriginalFilename();
		String realPath = request.getSession().getServletContext().getRealPath("/resources/userImg");
			System.out.println("실제파일 저장 경로 : " + realPath);
		if(!StringUtils.isNullOrEmpty(tmpUserProfileName)) {
			String ext = tmpUserProfileName.substring(tmpUserProfileName.lastIndexOf('.')+1);
			registMember.setUserImg(registMember.getUserId() + "." + ext);
		} 
		
		try {
			if(mService.saveMember(registMember)) {
				// 회원가입 성공시 홈화면으로
				redirectAttributes.addAttribute("status", "success");
				// 프로필파일 업로드여부 확인
				if(!StringUtils.isNullOrEmpty(tmpUserProfileName)) {
					fp.saveUserProfileFile(userProfile.getBytes(), realPath, registMember.getUserImg());
				}
			}
		} catch (Exception e) { // IOException, SQLException(DB작업시 발생한 예외)
			// 가입실패시 회원가입페이지 재로드
			e.printStackTrace();
			if(e instanceof IOException) {
				redirectAttributes.addAttribute("status", "fileFail");
				// DB에 회원가입한 유저 registMember.getUserId() 회원가입취소
				// service > dao
			} else {
				redirectAttributes.addAttribute("status", "fail");
			}  
			resultPage = "redirect:/member/register";
		}
		System.out.println("회원가입 진행!"+registMember.toString());
		return resultPage;
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
	
	@RequestMapping(value="/callSendMail")
	public ResponseEntity<String> sendMailAuthCode(@RequestParam("tmpUserEmail") String tmpUserEmail, HttpSession session) {
		String authCode = UUID.randomUUID().toString();
		System.out.println(tmpUserEmail + "로" + authCode + "인증코드 보내자~");
		String result = "";
		// 이메일 전송을 완료한 뒤에 코드를 세션영역에 저장
		try {
//			new SendMailService().sendMail(tmpUserEmail, authCode); // 실제 메일 보내는 기능!
			// 인증코드를 세션객체에 저장
			session.setAttribute("authCode", authCode);
			
			result = "success";
		} catch (Exception e) {
			// 메일발송에 실패했을 경우
			e.printStackTrace();
			result = "fail";
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	@RequestMapping("/checkAuthCode")
	public ResponseEntity<String> checkAuthCode(@RequestParam("tmpUserAuthCode")String tmpUserAuthCode, HttpSession session) {
		System.out.println(tmpUserAuthCode + "코드 잘 확인했어요~ 이제 비교도 하세요~~~");
		String result = "fail";
		if(session.getAttribute("authCode")!=null) {
			String sesAuthCode = (String)session.getAttribute("authCode");
			if(tmpUserAuthCode.equals(sesAuthCode)) {
				result = "success";
			}
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	@RequestMapping("/clearAuthCode")
	   public ResponseEntity<String> clearCode(HttpSession session) {
	      if (session.getAttribute("authCode") != null) {
	         session.removeAttribute("authCode");  // attribute 속성을 지운다...
	      }
	      
	      return new ResponseEntity<String>("success", HttpStatus.OK);
	   }
	@RequestMapping("/login")
	public void loginGET() {
		
	}
	@RequestMapping(value="/login", method=RequestMethod.POST) 
	public void loginPOST (LoginDTO loginDTO, Model model){
		System.out.println(loginDTO);
		try {
			MemberVO loginMember = mService.login(loginDTO);
			if(loginMember != null) {
				System.out.println("MemberController : 로그인 성공~");
				// 모델에 로그인정보 바인딩해서 인터셉터로 이동~
				model.addAttribute("loginMember", loginMember);
			} else {
				System.out.println("MemberController : 로그인 실패...");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
