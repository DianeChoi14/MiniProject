package com.miniproj.service.hboard;

import java.util.List;
import java.util.Map;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.HReplyBoardDTO;
import com.miniproj.model.PagingInfoDTO;
import com.miniproj.model.SearchCriteriaDTO;
import com.miniproj.model.SeoulTempVO;

//// Service단에서 해야할 작업
//1) Controller단에서 넘겨진 파라미터를 처리한 후(비즈니스 로직에 의해(트랜잭션처리를 통해))
//2) DB작업이라면 DAO단 호출
//3) DAO단에서 반환된 값을 Controller단으로 넘겨줌


// 클래스를 구현하기 전 메서드의 집합
public interface HBoardService 
{
	// 브레이스가 없는 메서드 추상메서드 > 구현체(impl)이 필요함
	// 게시판 전체 리스트 조회..하면
	public Map<String, Object> getAllBoard(PagingInfoDTO dto, SearchCriteriaDTO searchCriteria) throws Exception; // 라는 메서드를 조회한다(impl)~

	// 게시판 글 작성
	public boolean saveBoard(HBoardDTO newBoard) throws Exception;
	
	// 게시판 글 수정
	public boolean modifyBoard(HBoardDTO modifyBoard) throws Exception;	
	
	// 게시판 상세 보기
	public List<BoardDetailInfo> read(int boardNo, String  ipAddr) throws Exception;
	
	// 게시글 수정을 위해 게시글을 불러오는 메서드, read메서드 오버로딩
	
	/**
	 * @작성자 : 802-05
	 * @작성일 : 2024. 8. 8.
	 * @method_name : read
	 * @param : boardNo - 조회할 글 번호
	 * @return : List<BoardDetailInfo> : 글과 첨부파일(리스트), 작성자 정보를 함께 불러온다
	 * @throws : DAO단 에러
	*/
	public List<BoardDetailInfo> read(int boardNo) throws Exception;
	
	// 게시판 글 삭제
	public List<BoardUpFilesVODTO> removeBoard(int boardNo) throws Exception;
	
	// 게시글 답글달기
	public boolean saveReply(HReplyBoardDTO replyBoard) throws Exception;

	// 인기글 5개 가져오기
	public List<HBoardVO> getPopularBoards() throws Exception;

	// 차트연습
	public List<SeoulTempVO> getSeoulTemp() throws Exception;

	// 게시판 페이징

	
	// 게시글 검색
	
	
}
