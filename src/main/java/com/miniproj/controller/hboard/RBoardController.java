package com.miniproj.controller.hboard;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.PagingInfo;
import com.miniproj.model.PagingInfoDTO;
import com.miniproj.model.SearchCriteriaDTO;
import com.miniproj.service.hboard.RBoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rboard")
public class RBoardController {
	
	private static Logger logger = LoggerFactory.getLogger(HboardController.class);
	
	private final RBoardService service;
	
	@RequestMapping("/listAll")
	public void listAll(Model model, @RequestParam(value="pageNo", defaultValue = "1") int pageNo, @RequestParam(value="pagingSize", defaultValue="10") int pagingSize, SearchCriteriaDTO searchCriteria) {
		logger.info("페이징 사이즈 "+pagingSize + "씩 " + pageNo + "번 페이지 출력, " + "검색조건 :" + searchCriteria.toString());
		System.out.println("RBoardController.listAll()~");
		
		PagingInfoDTO dto =  PagingInfoDTO.builder()
			.pageNo(pageNo)
			.pagingSize(pagingSize)
			.build();
		
		List<HBoardVO> list = null;
		Map<String, Object> result = null;
		
		// 서비스단 호출
		try {
			result = service.getAllBoard(dto, searchCriteria);
			PagingInfo pi = (PagingInfo) result.get("pagingInfo");
			list = (List<HBoardVO>) result.get("boardList");
			model.addAttribute("pagingInfo", pi); // 데이터 바인딩
			model.addAttribute("boardList", list); // 데이터 바인딩
			model.addAttribute("search", searchCriteria); // 데이터 바인딩
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", "error");
			
		}
		
	}
	@RequestMapping("/showSaveBoardForm")
	public String showSaveBoardForm() {
		return "/rboard/saveBoardForm";
	}
	
	@RequestMapping(value="/saveBoard", method=RequestMethod.POST)
	public void saveBoard(HBoardDTO newBoard) {
		System.out.println(newBoard + "댓글형 게시판에 글을 저장하자~");
	}
}
	
