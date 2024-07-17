package com.miniproj.controller.hboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.service.hboard.HBoardService;
import com.miniproj.util.FileProcess;

//// 컨트롤러단에서 해야할 일 
//1) uri맵핑(어떤 uri가 어떤방식(get/post)으로 호출되었을 때 어떤 메서드에 매핑시킬 것인가)
//2) 있다면 view단에서 보내준 매개변수 수집
//3) 데이터베이스에 대한 CRUD수행하기 위해 서비스단 매서드 호출
//4) 서비스단에서 리턴한 값을 뷰단으로 바인딩, 뷰단 호출
//5) 부가적으로 컨트롤러단은 Servlet에 의해 동작하는 모듈이기 때문에 HttpServletRequest, HttpServletResponse, HttpSession등의
//	 Servlet객체들을 이용할 수 있다 > 이러한 객체들을 이용하여 구현할 기능이 있다면그 기능은 Controller단에서 구현한다

@Controller // 아래의 클래스가 컨트롤러 객체임을 명시
@RequestMapping("/hboard")
public class HboardController {
	// Log를 남길 수 있도록 하는 객체
	private static Logger logger = LoggerFactory.getLogger(HboardController.class);

	@Autowired
	private HBoardService service;

	@Autowired
	private FileProcess fileProcess;

	// 유저가 업로드한 파일을 임시 저장하는 객체
	private List<BoardUpFilesVODTO> uploadFileList = new ArrayList<BoardUpFilesVODTO>(); // List는 인터페이스, new ArrayList는 클래스
	
	// 게시판전체목록리스트를 출력하는 메소드
	@RequestMapping("/listAll")
	public void listAll(Model model) {
		// logger.info("HBoardController.listAll()~");
		System.out.println("HBoardController.listAll()~");

		// 서비스단 호출
		List<HBoardVO> list = null;
		try {
			list = service.getAllBoard();
			model.addAttribute("boardList", list); // 데이터 바인딩

		} catch (Exception e) {
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
	public String showSaveBoardForm() {
		return "/hboard/saveBoardForm";
	}

	// 게시글 저장버튼을 눌렀을 때 해당 게시글을 db에 저장하는 메서드 > 작성 후 글목록페이지로 이동
	@RequestMapping(value = "/saveBoard", method = RequestMethod.POST) // get방식은 메서드방식 생략가능
	public String saveBoard(HBoardDTO boardDTO, RedirectAttributes redirectAttributes) throws Exception {
		String returnPage = "redirect:/hboard/listAll";
		try {
			if (service.saveBoard(boardDTO)) { // 게시글 저장에 성공했을 때
				redirectAttributes.addAttribute("status", "success");
			}
		} catch (Exception e) { // 게시글 저장에 실패했을 때
			e.printStackTrace();
			redirectAttributes.addAttribute("status", "fail");
		}
		return returnPage; // 게시글 전체 목록 페이지로 돌아감
	}

	@RequestMapping(value = "/upfiles", method = RequestMethod.POST, produces = "text/plain; charset=UTF-8;")
	public ResponseEntity<String> saveBoardFile(@RequestParam("file")MultipartFile file, HttpServletRequest request) {
		// 리퀘스트 객체는 서블릿에서만 동작
		System.out.println("파일 전송됨 ... 저장해야함");

		ResponseEntity<String> result = null;
		// 파일의 기본정보 가져옴
		String contentType = file.getContentType();
		String originalFileName = file.getOriginalFilename();
		long fileSize = file.getSize();
		byte[] upFile = null;
		try {
			upFile = file.getBytes(); // 파일의 실제 데이터를 읽어옴
			// 요청의 http세션 객체를 얻어온뒤 서블릿을 얻고난 뒤에 경로얻기 가능
			String realPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");
			BoardUpFilesVODTO fileInfo = fileProcess.saveFileToRealPath(upFile, realPath, contentType, originalFileName, fileSize);
			System.out.println(
				"서버의 실제 경로 : " + request.getSession().getServletContext().getRealPath("/resources/boardUpFiles"));
			System.out.println("저장된 파일의 정보 : " + fileInfo.toString());
			
			this.uploadFileList.add(fileInfo);
			
			
			System.out.println("=============================================");
			System.out.println("========현재 파일리스트에 있는 파일들=========");
			for (BoardUpFilesVODTO f : this.uploadFileList) {
				System.out.println( f.toString());
			}
			System.out.println("=============================================");
			
			String tmp = fileInfo.getNewFileName().substring(fileInfo.getNewFileName().lastIndexOf(File.separator)+1);
			result = new ResponseEntity<String>("success_" + tmp, HttpStatus.OK); // EnumClass : sf값만 가질 수 있는 클래스
			
		} catch (IOException e) {
			// 저장실패시 오게되는 곳
			e.printStackTrace();
			result = new ResponseEntity<String>("fail", HttpStatus.NOT_ACCEPTABLE);
		}
		// request.getRealPath("/resources/boardUpFiles"); // getRealPath서버에 있는 물리적 경로를 제공
		return result; 

	}
	
	@RequestMapping(value = "/removefile", method=RequestMethod.POST)
	public void removeUpFile(@RequestParam("removedFileName") String removedFileName) {
		System.out.println("업로드된 파일 삭제하는 곳~~ : " + removedFileName);
		// 넘겨진 removeFileName과 uploadFileList배열의 originalFileName과 비교하여 같은 것이 있다면 삭제처리함(배열,하드디스크)
		for(int i=0 ; i<this.uploadFileList.size() ; i++) { //삭제할 파일의 경로와 이름을 주면서 delete()
			if(removedFileName.equals(this.uploadFileList.get(i).getOriginalFileName())) {
				// 하드디스크에서 삭제
				
			}
		}
	}
}
