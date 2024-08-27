package com.miniproj.aop;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.miniproj.model.LoginDTO;
import com.miniproj.model.MemberVO;
import com.miniproj.persistence.MemberDAO;
import com.miniproj.util.GetClientIPAddr;

/**
 * @작성자 : 802-05
 * @작성일 : 2024. 8. 26.
 * @프로젝트명 : MiniProject
 * @패키지명 : com.miniproj.aop
 * @파일명 : LoginlogAOP.java
 * @클래스명 : LoginlogAOP
 * @description : 로그인하는 멤버의 정보를 얻어내어 그 정보를 텍스트파일로 저장(로그인 시도하는 유저의 로그를 남긴다)
 * 				  로그인 과정의 프로세스 흐름 > 로그인은 인터셉터와 AOP의 조합이므로 아래 흐름을 잘 파악하여 코딩해야한다
 * 				  MemberServiceImpl.login() 끝난 후 > AOP(로그인 실패) > Controller > LoginInterceptor.postHandle()
 */
@Component
@Aspect
public class LoginlogAOP {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginlogAOP.class);
	private String logContent;
	static private Map<String, Integer> tryLoginCount = new ConcurrentHashMap<String, Integer>();
	
	@Autowired
	private MemberDAO mDao; // AOP는 스프링영역이므로 DAO단에 접근 가능
	
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
		// 현재 Advice에 걸려있는 target 메서드가 진행되도록 호출 > target메서드가 반환하는 값(MemberVO)도 가져옴
		Object result = pjp.proceed(); 
		
		if(result == null) {
			System.out.println("Around AOP : 로그인 실패");
			// 아이디, 횟수 > ArrayList와 HashMap은 thread safety하지 않다 > ConcurrentMap 사용 
			this.logContent += "," + "login Fail";
			result = saveLoginFail(who, result);
		} else {
			System.out.println("Around AOP : 로그인 성공");
			this.logContent += "," + "login Success";
			removeLoginFail(who);
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

	private void removeLoginFail(String who) {
		// 로그인에 실패하다 성공했을 때 이전의 기록을 삭제
		if(tryLoginCount.containsKey(who)) {
			tryLoginCount.remove(who);
		}
		
		outputTryLoginCount();
	}

	private Object saveLoginFail(String failedUserId, Object result) throws Exception { // 로그인이 실패했을 때 아이디와 실패 횟수를 저장
	      
	      // 기존에 로그인 실패 기록이 있다면 기존의 횟수 +1 후 다시 저장
	      if(tryLoginCount.containsKey(failedUserId)) { // tryLoginCount은 static한 메소드이기 때문에 this.이 아니라 클래스명을 붙여줘야 하는데 같은 클래스 내에 있기 때문에 굳이 붙이지 않아도 된다.
	         
	         int beforeFailedCount = tryLoginCount.get(failedUserId);
	         
	         ++beforeFailedCount; // 로그인 횟수 1 증가
	         
	         if (beforeFailedCount <= 4) { // 5번 시도 동안 로그인을 실패한다면
	            
	            tryLoginCount.put(failedUserId, beforeFailedCount);
	            
	         } else if(beforeFailedCount == 5) { // 로그인 시도 횟수를 초과한 유저이므로 계정을 잠궈야 한다.
	            System.out.println(failedUserId + "님. 로그인 시도 횟수가 초과되었습니다. 계정이 잠깁니다.");
	            
	            
	            mDao.updateAccountLock(failedUserId); // 계정을 잠궜다.
	            removeLoginFail(failedUserId); // 계정 잠금 후 로그인 실패 기록을 삭제해준다.
	            
	            MemberVO memberVO = (MemberVO)result;
	            
	            result = MemberVO.builder()
	                  .userId(failedUserId)
	                  .islock("Y")
	                  .build();
	         }
	         
	      } else { // 기존에 로그인 실패 기록이 없다면 처음 실패한 것이므로 실패 횟수에 1을 넣어주면 된다.
	         tryLoginCount.put(failedUserId, 1);
	      }
	      
	      outputTryLoginCount();
	      
	      return result;
	   }

	private void outputTryLoginCount() {
		Set<String> keys = tryLoginCount.keySet();
		for(String k : keys) {
			System.out.println("=============== 로그인 시도 횟수 카운트 : " + k + " - " + tryLoginCount.get(k) + " ================");
		}
	}
}
