package com.miniproj.persistence;

import java.util.List;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;

public interface HBoardDAO
{
	// 게시판의 전체 리스트를 가져오는 메서드
	List<HBoardVO> selectAllBoard() throws Exception; // 인터페이스에서는 public 생략가능
	
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

}
