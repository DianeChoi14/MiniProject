package com.miniproj.aop;

import org.apache.catalina.tribes.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component // 스프링컨테이너에게 객체를 만들어 관리하도록 설정하는 어노테이션
@Aspect // 아래의 객체를 AOP객체로 사용할 것임을 명시하는 어노테이션
public class ExampleAdvice {
	// Advice : AOP로 구현할 객체를 명시
	private static final Logger logger = LoggerFactory.getLogger(ExampleAdvice.class);
	
	// 인터셉터 - 특정 uri를 실행할 때 작동 됨, 서블릿(컨트롤러단)에 의해 동작하므로..
	// AOP - 특정틀래스-메서드를 실행할 때 작동 됨, 스프링(서비스단)에서 동작한다
	@Before("execution(public * com.miniproj.service.hboard.HBoardServiceImpl.saveBoard(..))")
	public void startAOP(JoinPoint jp) {
		System.out.println("============================== AOP 시작 =================================");
		Object[] params = jp.getArgs(); // 걸려있는 메서드의 매개변수를 object배열로 가져온다 
		System.out.println(Arrays.toString(params));
	}
	
	@After("execution(public * com.miniproj.service.hboard.HBoardServiceImpl.saveBoard(..))")
	public void endAOP() {
		System.out.println("================================ AOP 끝 =================================");
	}
}
