package com.miniproj.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.miniproj.model.HBoardVO;
import com.miniproj.model.MyResponseWithoutData;
import com.miniproj.service.hboard.HBoardService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller // 아래의 클래스가 컨트롤러 객체임을 명시
public class HomeController 
{
	// log를 남길 수 있도록 하는 객체
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private HBoardService service;
	
	@Autowired
	private HBoardService hbService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, HttpSession ses) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		if(ses.getAttribute("destPath") != null) {
			ses.removeAttribute("destPath");
		}
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "index";
	}
	
	@RequestMapping("/weather")
	public void goWeatherPage() {
		
	}
	
	@RequestMapping("/movie")
	public void goMoviePage() {
		
	}
	@RequestMapping("/news")
	public void goNewsPage() {
		
	}
	@RequestMapping("/saveCookie")
	public ResponseEntity<String> saveCookie(HttpServletResponse response) {
		System.out.println("쿠키를 저장하자~~~~");
		Cookie myCookie = new Cookie("notice", "N"); // name, value
		myCookie.setMaxAge(60*60*24); // 쿠키만료일 설정(초단위), 만료일이 되면 자동으로 쿠키가 삭제된다
		response.addCookie(myCookie);
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
		
	}
	@RequestMapping(value="/readCookie", produces = "application/json; charset=UTF-8;")
	public ResponseEntity<MyResponseWithoutData> readCookie(HttpServletRequest request) {
		System.out.println("쿠키를 읽어보자");
		Cookie[] cookies = request.getCookies(); // 쿠키 전체를 배열로 얻어옴
		
		MyResponseWithoutData result = null;
		
		// 이름이 notice인 쿠키 찾기
		for(int i=0 ; i<cookies.length ; i++) {
			if(cookies[i].getName().equals("notice") && cookies[i].getValue().equals("N")) {
				// 이름이 notice인 쿠키가 있고 그 값이 N이다
				result = new MyResponseWithoutData(200, null, "success");
				
			}
		}
		
		if(result == null) {
			result = new MyResponseWithoutData(400, null, "fail");
		}
		
		return new ResponseEntity<MyResponseWithoutData>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value="/get5Board", produces = "application/json; charset=UTF-8;")
	public ResponseEntity<List<HBoardVO>> get5Board() {
		ResponseEntity<List<HBoardVO>> result = null; 
		try {
			List<HBoardVO> popBoards = hbService.getPopularBoards();
			result = new ResponseEntity<List<HBoardVO>>(popBoards, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		return result;
	}
	// "/SampleInterceptor.jsp" 를 찾아 request, response
	@RequestMapping("/sampleInterceptor")
	public void sampleInterceptor() {
		
		System.out.println("sampleInterceptor() 호출!!!!!");
	}
}
