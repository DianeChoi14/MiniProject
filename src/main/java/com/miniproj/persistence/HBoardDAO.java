package com.miniproj.persistence;

import java.util.List;

import com.miniproj.model.HBoardVO;

public interface HBoardDAO
{
	// 게시판의 전체 리스트를 가져오는 메서드
	List<HBoardVO> selectAllBoard(); // 인터페이스에서는 public 생략가능
}
