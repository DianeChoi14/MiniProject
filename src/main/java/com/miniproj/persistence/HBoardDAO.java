package com.miniproj.persistence;

import java.util.List;

import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;

public interface HBoardDAO
{
	// 게시판의 전체 리스트를 가져오는 메서드
	List<HBoardVO> selectAllBoard() throws Exception; // 인터페이스에서는 public 생략가능
	
	// 게시글을 저장하는 메서드
	int insertNewBoard(HBoardDTO newBoard);
}
