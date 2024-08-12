package com.miniproj.persistence;

import java.util.List;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.HReplyBoardDTO;
import com.miniproj.model.PagingInfo;
import com.miniproj.model.SearchCriteriaDTO;

public interface RBoardDAO  {
	// 게시판의 전체 리스트를 가져오는 메서드
	List<HBoardVO> selectAllBoard(PagingInfo pi) throws Exception; // 인터페이스에서는 public 생략가능
		
	// 게시글을 저장하는 메서드
	int insertNewBoard(HBoardDTO newBoard);
		
	// 최근 저장된 글의 글 번호를 얻어오는 메서드
	int getMaxBoardNo() throws Exception;
		
	// 업로드된 첨부파일을 저장하는 쿼리문
	int insertBoardUpFile(BoardUpFilesVODTO upFile) throws Exception;
		
	// 게시글의 상세정조블 얻어오는 메서드
	List<BoardDetailInfo> selectBoardByBoardNo(int boardNo) throws Exception;
		
	// 게시글의 조회수를 증가하는 메서드
	int updateReadCount(int boardNo);

	// ipAddr의 유저가 boardNo글을 조회한 시간차를 얻는 메서드 ( 조회한 적이 없으면 -1반환)
	int selectDateDiff(int boardNo, String ipAddr) throws Exception;

	// ipAddr의 유저가 boardNo글을 현재 시간에 조회한다고 기록
	int saveBoardReadLog(int boardNo, String ipAddr) throws Exception;

	// 조회수 증가한 날짜로 업데이트
	int updateReadWhen(int boardNo, String ipAddr) throws Exception;

	// 글 번호를 ref컬럼에 업데이트
	int updateBoardRef(int newBoardNo) throws Exception;

	// 답글 데이터 + ref, step, refOrder 값을 저장
	int insertReplyBoard(HReplyBoardDTO replyBoard) throws Exception;

	// 부모글에 대한 다른 답글이 있는 상태에서, 부모글에 답글이 추가되는 경우 (자리확보를 위해) 기존 답글의 refOrder값을 수정
	void updateBoardRef(int ref, int refOrder) throws Exception;
		
	// 첨부파일이 있다면 서버에서 삭제하기 전에 해당 글의 첨부파일 정보를 불러온다
	List<BoardUpFilesVODTO> selectBoardUpFiles (int boardNo) throws Exception;

	// boardupfiles에서 첨부파일을 모오두 삭제하는 메서드
	void deleteAllBoardUpFiles(int boardNo) throws Exception;

	// boardNo번 글을 삭제처리
	int deleteBoardByBoardNo(int boardNo) throws Exception ;
		
	// 수정된 순수 게시글 내용(title, content)을 수정하는 메서드 
	int updateBoardbyBoardNo(HBoardDTO modifyBoard) throws Exception;
		
	// 첨부파일의 PK번호(boardUpFileNo) 로 첨부파일을 일부만 삭제하는 메서드
	void deleteBoardUpFiles(int boardUpFileNo);
		
	// 인기글 5개 가져오기
	List<HBoardVO> selectPopBoards() throws Exception;

	// (검색어가 없을 때)게시글의 총 갯수를 가져오는 메서드
	int getTotalPostCnt() throws Exception;

	// 검색어가 있을 때, 검색결과의 총 갯수를 가져오는 메서드
	int getTotalPostCnt(SearchCriteriaDTO sc) throws Exception;

	// 검색어가 있을 때, 검색된 글들을 페이징하여 가져오는 메서드
	List<HBoardVO> selectAllBoard(PagingInfo pi, SearchCriteriaDTO searchCriteria) throws Exception;
}
