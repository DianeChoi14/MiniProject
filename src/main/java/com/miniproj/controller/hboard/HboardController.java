package com.miniproj.controller.hboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.service.hboard.HBoardService;

//// 컨트롤러단에서 해야할 일 
//1) uri맵핑(어떤 uri가 어떤방식(get/post)으로 호출되었을 때 어떤 메서드에 매핑시킬 것인가)
//2) 있다면 view단에서 보내준 매개변수 수집
//3) 데이터베이스에 대한 CRUD수행하기 위해 서비스단 매서드 호출
//4) 서비스단에서 리턴한 값을 뷰단으로 바인딩, 뷰단 호출
//5) 부가적으로 컨트롤러단은 Servlet에 의해 동작하는 모듈이기 때문에 HttpServletRequest, HttpServletResponse, HttpSession등의
//	 Servlet객체들을 이용할 수 있다 > 이러한 객체들을 이용하여 구현할 기능이 있다면그 기능은 Controller단에서 구현한다

@Controller // 아래의 클래스가 컨트롤러 객체임을 명시
@RequestMapping("/hboard")
public class HboardController 
{
   // Log를 남길 수 있도록 하는 객체
   private static Logger logger = LoggerFactory.getLogger(HboardController.class);
   
   @Autowired
   private HBoardService service;
   
   //게시판전체목록리스트를 출력하는 메소드
   @RequestMapping("/listAll")
   public void listAll(Model model)
   {
	   //logger.info("HBoardController.listAll()~");
	   System.out.println("HBoardController.listAll()~");
	   
	   // 서비스단 호출
	List<HBoardVO> list=null;
	try 
	{
		list = service.getAllBoard();
		model.addAttribute("boardList", list); // 데이터 바인딩
		
	} 
	catch (Exception e) 
	{
		model.addAttribute("exception", "error");
	}
	   
//		for (HBoardVO b : list)
//		{
//			System.out.println(b.toString());
//		}
	   
	   // 'hboard/listAll.jsp'로 포워딩 됨
	   
   }
   
   // 게시판 글 저장 폼을 출력하는 메서드
   @RequestMapping("/saveBoard")
   public String showSaveBoardForm()
   {
	   return "/hboard/saveBoardForm";
   }
   
   // 게시글 저장버튼을 눌렀을 때 해당 게시글을 db에 저장하는 메서드
   @RequestMapping(value="/saveBoard", method=RequestMethod.POST) // get방식은 메서드방식 생략가능 
   public void saveBoard(HBoardDTO boardDTO) throws Exception
   {
	   System.out.println("이 게시글을 저장하자~!~~~"+boardDTO.toString());
	   service.saveBoard(boardDTO);
   }
}
