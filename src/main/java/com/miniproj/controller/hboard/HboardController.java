package com.miniproj.controller.hboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
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

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFileStatus;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.HReplyBoardDTO;
import com.miniproj.model.MyResponseWithoutData;
import com.miniproj.service.hboard.HBoardService;
import com.miniproj.util.FileProcess;
import com.miniproj.util.GetClientIPAddr;

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
	private List<BoardUpFilesVODTO> uploadFileList = new ArrayList<BoardUpFilesVODTO>(); // List는 인터페이스, new ArrayList는
																							// 클래스
	private List<BoardUpFilesVODTO> modifyFileList;

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
		boardDTO.setFileList(this.uploadFileList); // 첨부파일리스트를 BoardDTO에 주입

		String returnPage = "redirect:/hboard/listAll";
		try {
			if (service.saveBoard(boardDTO)) { // 게시글 저장에 성공했을 때
				redirectAttributes.addAttribute("status", "success");

			}
		} catch (Exception e) { // 게시글 저장에 실패했을 때
			e.printStackTrace();
			redirectAttributes.addAttribute("status", "fail");
		}
		this.uploadFileList.clear();
		return returnPage; // 게시글 전체 목록 페이지로 돌아감
	}

	@RequestMapping(value = "/upfiles", method = RequestMethod.POST, produces = "application/json; charset=UTF-8;")
	public ResponseEntity<MyResponseWithoutData> saveBoardFile(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) {
		// 리퀘스트 객체는 서블릿에서만 동작
		System.out.println("파일 전송됨 ... 저장해야함");

		ResponseEntity<MyResponseWithoutData> result = null;

		try {
			BoardUpFilesVODTO fileInfo = fileSave(file, request);

			System.out.println(
					"서버의 실제 경로 : " + request.getSession().getServletContext().getRealPath("/resources/boardUpFiles"));
			System.out.println("저장된 파일의 정보 : " + fileInfo.toString());

			this.uploadFileList.add(fileInfo);

			System.out.println("=============================================");
			System.out.println("========현재 파일리스트에 있는 파일들=========");
			for (BoardUpFilesVODTO f : this.uploadFileList) {
				System.out.println(f.toString());
			}
			System.out.println("=============================================");

			String tmp = null;
			if (fileInfo.getThumbFileName() != null) {
				// 이미지
				tmp = fileInfo.getThumbFileName();
			} else {
				tmp = fileInfo.getNewFileName().substring(fileInfo.getNewFileName().lastIndexOf(File.separator) + 1);
			}

			// 저장된 새로운 파일이름을 제이슨으로 리턴

			MyResponseWithoutData mrw = MyResponseWithoutData.builder().code(200).msg("success").newFileName(tmp)
					.build();
			result = new ResponseEntity<MyResponseWithoutData>(mrw, HttpStatus.OK); // EnumClass : sf값만 가질 수 있는 클래스

		} catch (IOException e) {
			// 저장실패시 오게되는 곳
			e.printStackTrace();
			result = new ResponseEntity<>(HttpStatus.OK);
		}
		// request.getRealPath("/resources/boardUpFiles"); // getRealPath서버에 있는 물리적 경로를
		// 제공
		return result;

	}

	private BoardUpFilesVODTO fileSave(MultipartFile file, HttpServletRequest request) throws IOException {
		// 파일의 기본정보 가져옴
		String contentType = file.getContentType();
		String originalFileName = file.getOriginalFilename();
		long fileSize = file.getSize();
		byte[] upFile = file.getBytes(); // 파일의 실제 데이터를 읽어옴
		// 요청의 http세션 객체를 얻어온뒤 서블릿을 얻고난 뒤에 경로얻기 가능
		String realPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");

		// 실제파일저장(이름변경, base64, thumbnail)
		BoardUpFilesVODTO fileInfo = fileProcess.saveFileToRealPath(upFile, realPath, contentType, originalFileName,
				fileSize);

		return fileInfo;
	}

	@RequestMapping(value = "/removefile", method = RequestMethod.POST, produces = "application/json; charset=UTF-8;")
	public ResponseEntity<MyResponseWithoutData> removeUpFile(@RequestParam("removedFileName") String removedFileName,
			HttpServletRequest request) {
		System.out.println("업로드된 파일 삭제하는 곳~~ : " + removedFileName);

		boolean removeResult = false;
		ResponseEntity<MyResponseWithoutData> result = null;
		int removeIndex = -1;

		// 넘겨진 removeFileName과 uploadFileList배열의 originalFileName과 비교하여 같은 것이 있다면
		// 삭제처리함(배열,하드디스크)
		for (int i = 0; i < this.uploadFileList.size(); i++) { // 삭제할 파일의 경로와 이름을 주면서 delete()
			if (uploadFileList.get(i).getNewFileName().contains(removedFileName)) {
				// 하드디스크에서 삭제
				System.out.println(i + "번째에서 해당파일 찾앗다!" + uploadFileList.get(i).getNewFileName());
				// uploadFileList.remove(i);
				String realPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");
				if (fileProcess.removeFile(realPath + uploadFileList.get(i).getNewFileName())) {
					removeIndex = i;
					removeResult = true;
					break;
				}
			}
		}
		if (removeResult) {
			uploadFileList.remove(removeIndex);
			System.out.println("=============================================");
			System.out.println("========현재 파일리스트에 있는 파일들=========");
			for (BoardUpFilesVODTO f : this.uploadFileList) {
				System.out.println(f.toString());
			}
			System.out.println("=============================================");

			result = new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(200, "", "success"),
					HttpStatus.OK);
		} else {
			result = new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(400, "", "fail"),
					HttpStatus.CONFLICT);
		}

		return result;

	}

	private void outputCurModifyFileList() {
		System.out.println("=============================================");
		System.out.println("========글 수정 파일리스트에 있는 파일들=========");
		for (BoardUpFilesVODTO f : this.modifyFileList) {
			System.out.println(f.toString());
		}
		System.out.println("=============================================");
	}

//================================================================================================================================
	private void allUploadFileDelete(String realPath, List<BoardUpFilesVODTO> fileList) {
		for (int i = 0; i < fileList.size(); i++) {
			fileProcess.removeFile(realPath + uploadFileList.get(i).getNewFileName());

			// 이미지파일일 경우 썸네일파일까지 삭제함
			if (uploadFileList.get(i).getThumbFileName() != null || fileList.get(i).getThumbFileName() != "") {
				fileProcess.removeFile(realPath + fileList.get(i).getThumbFileName());
			}
		}
	}

	@RequestMapping(value = "/cancelBoard", method = RequestMethod.GET, produces = "application/json; charset=UTF-8;")
	public ResponseEntity<MyResponseWithoutData> cancelBoard(HttpServletRequest request) {
		System.out.println("유저가 업로드한 모든 파일을 삭제");
		String realPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");
		// 페이지에 들어갈 때마다 리스트객체가 new되므로 =!null으로 표현식 불가
		if (this.uploadFileList.size() > 0) {
			allUploadFileDelete(realPath, this.uploadFileList);
			this.uploadFileList.clear();
		}
		return new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(200, "", "success"), HttpStatus.OK);
	}

	@RequestMapping("/removeBoard")
	public String removeBoard(@RequestParam("boardNo") int boardNo, RedirectAttributes redirectattributes,
			HttpServletRequest request) {
		System.out.println(boardNo + "번 글을 삭제하려고요~~~~~");
		try {
			List<BoardUpFilesVODTO> fileList = service.removeBoard(boardNo);
			// 서비스단에서 리스트객체가 생성되어 반환되기 때문에 첨부파일이 없어도 빈 리스트 객체를 반환함 > null이 아니다!!
			// Optional : 객체 안에 데이터가 있는지 확인하는 클래스 ; Optional<List<BoardUpFilesVODTO>> t;로
			// null인지 확인 가능
			String realPath = request.getSession().getServletContext().getRealPath("/resources/boardUpFiles");

			if (fileList.size() > 0) {
				allUploadFileDelete(realPath, fileList);
			}
			redirectattributes.addAttribute("status", "success");

		} catch (Exception e) {
			e.printStackTrace();
			redirectattributes.addAttribute("status", "fail");

		}
		return "redirect:/hboard/listAll";
	}

//================================================================================================================================

	// 아래의 viewBoard() 메서드는 /viewBoard, /modifyBoard(게시글 수정을 위해 게시글 호출) 기능일 때 두 경우에
	// 호출된다
	@RequestMapping(value = { "/viewBoard", "/modifyBoard" })
	public String viewBoard(@RequestParam("boardNo") int boardNo, Model model, HttpServletRequest request) {

		String returnViewPage = "";
		String ipAddr = GetClientIPAddr.getClientIp(request);
		List<BoardDetailInfo> boardDetailInfo = null;

		System.out.println("=========" + ipAddr + "가" + boardNo + "글을 조회한다!=================");
		System.out.println("URI출력 : " + request.getRequestURI());
		// 수정페이지 호출 시에는 조회수 업데이트 X, 상세보기와 수정페이지는 뷰단이 다르다

		try {
			if (request.getRequestURI().equals("/hboard/viewBoard")) {
				System.out.println("게시판상세보기 호출~~~~~~~~~~~~~~~");
				returnViewPage = "/hboard/viewBoard";
				boardDetailInfo = service.read(boardNo, ipAddr);

			} else if (request.getRequestURI().equals("/hboard/modifyBoard")) {
				System.out.println("게시판 수정페이지 호출~~~~~~~~~~~~~~~~~~~~~~~~~");
				returnViewPage = "/hboard/modifyBoard";
				boardDetailInfo = service.read(boardNo);

				int fileCount = -1;
				for (BoardDetailInfo b : boardDetailInfo) {
					// DB에서 가저온 업로드된 파일리스트를 멤버변수에 할당
					fileCount = b.getFileList().size();
					this.modifyFileList = b.getFileList();
				}
				model.addAttribute("fileCount", fileCount);

				System.out.println("===================================================");
				System.out.println("========수정페이지 파일리스트에 있는 파일들=========");
				for (BoardUpFilesVODTO f : this.modifyFileList) {
					System.out.println(f.toString());
				}
				System.out.println("===================================================");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			returnViewPage = "redirect:/hboard/listAll?status=fail";
		}

		model.addAttribute("boardDetailInfo", boardDetailInfo);
		return returnViewPage;
	}

//	@RequestMapping("/modifyBoard")
//	public void modifyBoard(@RequestParam("boardNo") int boardNo, HttpServletRequest request) {
//		System.out.println("URI출력 : " + request.getRequestURI());
//	}

	@RequestMapping("/showReplyForm")
	public String showReplyForm() {
		return "/hboard/showReplyForm";

	}

	@RequestMapping(value = "/saveReply", method = RequestMethod.POST)
	public String saveReplyBoard(HReplyBoardDTO replyBoard, RedirectAttributes redirectattributes) {
		System.out.println(replyBoard + "를 답글로 저장하자........");
		String returnPage = "redirect:/hboard/listAll";
		try {
			if (service.saveReply(replyBoard)) {
				redirectattributes.addAttribute("status", "success");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			redirectattributes.addAttribute("status", "fail");
		}
		return returnPage;
	}

	@RequestMapping(value = "/modifyRemoveFileCheck", method = RequestMethod.POST, produces = "application/json; charset=UTF-8;")
	public ResponseEntity<MyResponseWithoutData> modifyRemoveFileCheck(
			@RequestParam("removeFileNo") int removedFilePK) {
		// 게시판이 수정되기 전에 파일을 하드에서 삭제할 수 없으므로 삭제될 파일을 미리 확인하는 것
		// 게시판이 최종 수정이 되면 실제 삭제처리해야한다..
		System.out.println(removedFilePK + "파일을 삭제처리하쟈~!");
		for (BoardUpFilesVODTO file : this.modifyFileList) {
			if (removedFilePK == file.getBoardUpFileNo()) {
				file.setFileStatus(BoardUpFileStatus.DELETE);
			}
		}
		System.out.println("===================================================");
		System.out.println("========수정페이지 파일리스트에 있는 파일들=========");
		for (BoardUpFilesVODTO f : this.modifyFileList) {
			System.out.println(f.toString());
		}
		System.out.println("===================================================");

		return new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(200, null, "success"),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/cancelRemFiles")
	public ResponseEntity<MyResponseWithoutData> cancelRemFiles() {
		for (BoardUpFilesVODTO file : this.modifyFileList) {
			file.setFileStatus(null);
		}
		return new ResponseEntity<MyResponseWithoutData>(new MyResponseWithoutData(200, null, "success"),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/modifyBoardSave", method = RequestMethod.POST)
	public String modifyBoardSave(HBoardDTO modifyBoard, @RequestParam("modifyNewFile") MultipartFile[] modifyNewFile,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
//		System.out.println(modifyBoard.toString() + "를 수정하쟈!! ");
		try {
			for (int i = 0; i < modifyNewFile.length; i++) {
				System.out.println(modifyNewFile[i].getOriginalFilename() + "..파일을 업로드도 할 거임!");

				BoardUpFilesVODTO fileInfo = fileSave(modifyNewFile[i], request);
				this.modifyFileList.add(fileInfo);
				fileInfo.setFileStatus(BoardUpFileStatus.INSERT); // insert돼야할 파일임을 기록
			}
			System.out.println("===================================================");
			System.out.println("========수정된 modifyFileList에 있는 파일들=========");
			for (BoardUpFilesVODTO f : this.modifyFileList) {
				System.out.println(f.toString());
			}
			System.out.println("===================================================");
			
			// next >> DB에 저장(service에 호출) IOException 보다 더 상위의 Exception으로 변경
			modifyBoard.setFileList(modifyFileList);
			
			if(service.modifyBoard(modifyBoard)) {
				redirectAttributes.addAttribute("status", "success");
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			redirectAttributes.addAttribute("status", "fail");
		}
		return "redirect:/hboard/viewBoard?boardNo=" + modifyBoard.getBoardNo();
	}
	
	
}
