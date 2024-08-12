package com.miniproj.controller.hboard;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.PagingInfo;
import com.miniproj.model.PagingInfoDTO;
import com.miniproj.model.SearchCriteriaDTO;
import com.miniproj.service.hboard.RBoardService;
import com.miniproj.util.GetClientIPAddr;

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
		// System.out.println("RBoardController.listAll()~");
		
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
	public String saveBoard(HBoardDTO newBoard, RedirectAttributes redirectAttributes) {
		// System.out.println(newBoard + "댓글형 게시판에 글을 저장하자~");
		String returnPage = "redirect:/rboard/listAll";
		try {
			if(service.saveBoard(newBoard)) {
				redirectAttributes.addAttribute("status", "success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addAttribute("status", "fail");
		}
		return returnPage;
	}
	
	@RequestMapping(value = { "/viewBoard", "/modifyBoard" })
	public String viewBoard(@RequestParam("boardNo") int boardNo, Model model, HttpServletRequest request) {

		String returnViewPage = "";
		String ipAddr = GetClientIPAddr.getClientIp(request);
		BoardDetailInfo boardDetailInfo = null;

		System.out.println("=========" + ipAddr + "가" + boardNo + "글을 조회한다!=================");
		System.out.println("URI출력 : " + request.getRequestURI());
		// 수정페이지 호출 시에는 조회수 업데이트 X, 상세보기와 수정페이지는 뷰단이 다르다

		try {
			if (request.getRequestURI().equals("/rboard/viewBoard")) {
				System.out.println("댓글형 게시판상세보기 호출~~~~~~~~~~~~~~~");
				returnViewPage = "/rboard/viewBoard";
				boardDetailInfo = service.read(boardNo, ipAddr);

			} else if (request.getRequestURI().equals("/rboard/modifyBoard")) {
//				System.out.println("댓글형 게시판 수정페이지 호출~~~~~~~~~~~~~~~~~~~~~~~~~");
//				returnViewPage = "/rboard/modifyBoard";
//				boardDetailInfo = service.read(boardNo);
//
//				int fileCount = -1;
//				for (BoardDetailInfo b : boardDetailInfo) {
//					// DB에서 가저온 업로드된 파일리스트를 멤버변수에 할당
//					fileCount = b.getFileList().size();
//					this.modifyFileList = b.getFileList();
//				}
//				model.addAttribute("fileCount", fileCount);
//
//				System.out.println("===================================================");
//				System.out.println("========수정페이지 파일리스트에 있는 파일들=========");
//				for (BoardUpFilesVODTO f : this.modifyFileList) {
//					System.out.println(f.toString());
//				}
//				System.out.println("===================================================");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			returnViewPage = "redirect:/rboard/listAll?status=fail";
		}

		model.addAttribute("board", boardDetailInfo);
		return returnViewPage;
	}
	
	
}
	
