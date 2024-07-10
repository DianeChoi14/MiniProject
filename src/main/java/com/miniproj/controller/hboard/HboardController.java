package com.miniproj.controller.hboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.miniproj.model.HBoardVO;
import com.miniproj.service.hboard.HBoardService;

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
	   List<HBoardVO> list = service.getAllBoard();
//		for (HBoardVO b : list)
//		{
//			System.out.println(b.toString());
//		}
	   
	   model.addAttribute("boardList", list); // 데이터 바인딩
	   
	   // 'hboard/listAll.jsp'로 포워딩 됨
	   
   }
   // 게시판 글 저장 폼을 출력하는 메서드
   @RequestMapping("/saveBoard")
   public String showSaveBoardForm()
   {
	   return "/hboard/saveBoardForm";
   }
   
}
