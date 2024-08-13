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
		
	// 게시글의 상세정보를 얻어오는 메서드
	BoardDetailInfo selectBoardByBoardNo(int boardNo) throws Exception;

	// boardNo번 글을 삭제처리
	int deleteBoardByBoardNo(int boardNo) throws Exception ;
		
	// 인기글 5개 가져오기
	List<HBoardVO> selectPopBoards() throws Exception;

	// (검색어가 없을 때)게시글의 총 갯수를 가져오는 메서드
	int getTotalPostCnt() throws Exception;

	// 검색어가 있을 때, 검색결과의 총 갯수를 가져오는 메서드
	int getTotalPostCnt(SearchCriteriaDTO sc) throws Exception;

	// 검색어가 있을 때, 검색된 글들을 페이징하여 가져오는 메서드
	List<HBoardVO> selectAllBoard(PagingInfo pi, SearchCriteriaDTO searchCriteria) throws Exception;
}
