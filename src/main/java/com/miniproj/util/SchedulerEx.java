package com.miniproj.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.miniproj.service.member.MemberService;

@Component
@EnableScheduling
public class SchedulerEx {
	//
	@Autowired
	private MemberService ms;
//	@Scheduled(cron="0/10 * * * * *")
//	public void testScheduler() {
//		System.out.println("======================================스케쥴러 작동======================================");
//		System.out.println(ms.toString());
//		
//	}
}
