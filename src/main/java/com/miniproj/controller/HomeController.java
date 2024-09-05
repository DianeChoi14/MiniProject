package com.miniproj.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.google.gson.Gson;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.MyResponseWithData;
import com.miniproj.model.MyResponseWithoutData;
import com.miniproj.model.SearchBookJSON;
import com.miniproj.service.hboard.HBoardService;
import com.miniproj.util.PropertiestTask;

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
		
		// properties에서 설정파일을 불러오기(아이디와 비밀번호 key들을 저장해놓고 불러오기 위한 용도)
		try {
			System.out.println(PropertiestTask.getPropertiesValue("key"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	@RequestMapping("/chartEx1")
	public String showChartPage() {
		return "/chartEx3" ;
	}
	
	@RequestMapping(value="/seoulTemp", produces = "application/json; charset=UTF-8;")
	public ResponseEntity getSeoulTemp() {
		ResponseEntity result = null; 
		try {
			result = new ResponseEntity(MyResponseWithData.success(hbService.getSeoulTemp()), HttpStatus.OK);
		} catch (Exception e) {
			
			e.printStackTrace();
			result = new ResponseEntity(MyResponseWithData.fail(), HttpStatus.BAD_REQUEST);
		}
		return result;
	}
	
	@RequestMapping("/mapEx1")
	public String showMapEx1() {
		return "/tour/tourList";
	}
	
	@RequestMapping("/tourSub")
	public String showMapEx1Sub() {
		return "/tour/tourSub";
	}
	
	@RequestMapping("/bookSearch")
	public String showBookSearch() {
		return "/bookSearch";
	}
	
	@RequestMapping(value="/searchBook", produces = "application/json; charset=utf-8")
	public @ResponseBody String searchBook(@RequestParam("searchValue") String query) {
		System.out.println(query + "책의 검색결과를 찾아보자~");
		String clientId = "_hhwmA2RY2Kci1filIV5"; //애플리케이션 클라이언트 아이디
        String clientSecret = "eXFNzAVgpU"; //애플리케이션 클라이언트 시크릿
        String text = null;
        try {
            text = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }
        String apiURL = "https://openapi.naver.com/v1/search/book.json?query=" + text; // json을 요청할 api주소
        
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        
        String responseBody = get(apiURL,requestHeaders); // url과 id secret을 담아서 보냄 > 응답 : json문자열

        System.out.println(responseBody);
        makeJavaObject(responseBody);
        // 이 응답결과는 DB에 존재하지 않으므로 insert시키자 > DTO객체로 만들자 : json문자열을 자바객체로 만든다
        return responseBody;
	}
	
	private void makeJavaObject(String responseBody) {
		Gson gson = new Gson();
		SearchBookJSON obj = gson.fromJson(responseBody, SearchBookJSON.class);
		System.out.println(obj.toString());
	}

	private String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET"); // 통신방식 GET으로 지정
            
            // Map을 반복하기 위해 Map.Entry<K,V>사용
            // 반복하여 request객체의 속성에 넣어줌
            for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue()); // api서버에 접속하기 위한 requestHeaders의 아이디와 비밀번호 요청
            }

            int responseCode = con.getResponseCode(); // api서버에 접속하여 응답코드를 얻어 옴
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream()); // 내 컴퓨터CPU로 (네이버API서버가 보내주는/응답하는)들어오는 2진 데이터 > readBody() 호출
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {// try catch여부와 상관없이 실행됨
            con.disconnect(); // api접속 해제
        }
    }
	
	// apiURL 주소로부터 그 주소의 서버에 접속할 수 있는 connection객체를 얻어서 반환해줌
	private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl); // String으로 된 서버의 주소를 실제 url객체로 만듦
            return (HttpURLConnection)url.openConnection(); // URLConnection : DB에서 
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }
	
    private String readBody(InputStream body){
        // Reader : 한 글자씩 읽음, 다른 작업 불가
    	InputStreamReader streamReader = null;
		try {
			streamReader = new InputStreamReader(body, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
        

        try (BufferedReader lineReader = new BufferedReader(streamReader)) { // try (객체) : 객체를 만드는데 성공한다면 실행문 실행
        	// BufferedReader메모리에서 읽은 글자를 조합해서 반환, 다른 작업 가능
            StringBuilder responseBody = new StringBuilder(); // StringBuilder객체 생성
            String line;
            while ((line = lineReader.readLine()) != null) { // 데이터의 끝이 아닐동안 반복하면서 읽음
                responseBody.append(line);
            }
            return responseBody.toString(); // json문자열
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

}