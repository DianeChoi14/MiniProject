package com.miniproj.aop;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.miniproj.model.LoginDTO;
import com.miniproj.util.GetClientIPAddr;

/**
 * @작성자 : 802-05
 * @작성일 : 2024. 8. 26.
 * @프로젝트명 : MiniProject
 * @패키지명 : com.miniproj.aop
 * @파일명 : LoginlogAOP.java
 * @클래스명 : LoginlogAOP
 * @description : 로그인하는 멤버의 정보를 얻어내어 그 정보를 텍스트파일로 저장(로그인 시도하는 유저의 로그를 남긴다)
 */
@Component
@Aspect
public class LoginlogAOP {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginlogAOP.class);
	private String logContent;
	
	// Around : Before와 After가 동시에 수행되도록하는 어노테이션
	@Around("execution(public * com.miniproj.service.member.MemberServiceImpl.login(..))")
	public Object betweenMemberLogin(ProceedingJoinPoint pjp) throws Throwable {
		logger.info("=========================== Around AOP Before ==============================");
		// =========================MemberServiceImple.login()가 실행되기 이전에 수행
		Object[] params = pjp.getArgs();
		LoginDTO loginDTO = (LoginDTO) params[0];
		logger.info("로그인시도하려는 유저 정보 : " + loginDTO.toString());
		
		long curTime = System.currentTimeMillis();
		String loginTime = new Date(curTime).toString();
		String who = loginDTO.getUserId();
		String ipAddr = loginDTO.getIpAddr();
		this.logContent = loginTime + "," + who + "," + ipAddr;
		Calendar now = Calendar.getInstance();
		
		String year = now.get(Calendar.YEAR) + ""; // '\2024'
		String month = year+ new DecimalFormat("00").format(now.get(Calendar.MONTH)+1); // '\2024\07'
		String when = month+ new DecimalFormat("00").format(now.get(Calendar.DATE)); // '2024\07\16'
		// System.out.println(this.logContent);
		
		logger.info("=========================== Around AOP After ==============================");
		// =========================MemberServiceImple.login()가 실행되고 이후에 수행
		// 현재 Advice에 걸려있는 target 메서드가 진행되도록 호출 > target메서드가 반환하는 값도 가져옴
		Object result = pjp.proceed(); 
		if(result == null) {
			System.out.println("Around AOP : 로그인 실패");
			this.logContent += "," + "login Fail";
		} else {
			System.out.println("Around AOP : 로그인 성공");
			this.logContent += "," + "login Success";
		}

		// loginLog.csv로 저장
		String logSavePath = "D:\\lecture\\spring\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\logs";
		FileWriter fw = new FileWriter(logSavePath + File.separator + "log_" + when + ".csv", true);
		fw.write(this.logContent);
		fw.write("\n");
		fw.flush();
		fw.close();
		
		return result; // target메서드 수행 후 반환되는 값을 다시 컨트롤러 단으로 돌려줌
	}
}
