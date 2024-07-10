package com.miniproj.service.hboard;

import java.util.List;

import com.miniproj.model.HBoardVO;

// 클래스를 구현하기 전 메서드의 집합
public interface HBoardService 
{
	// 브레이스가 없는 메서드 추상메서드 > 구현체(impl)이 필요함
	// 게시판 전체 리스트 조회..하면
	public List<HBoardVO> getAllBoard(); // 라는 메서드를 조회한다(impl)~

	// 게시판 글 작성
	
	// 게시판 글 수정
	
	// 게시판 상세 보기
	
	// 게시판 글 삭제
	
	
}
