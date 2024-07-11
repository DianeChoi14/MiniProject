package com.miniproj.service.hboard;

import java.util.List;

import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;

//// Service단에서 해야할 작업
//1) Controller단에서 넘겨진 파라미터를 처리한 후(비즈니스 로직에 의해(트랜잭션처리를 통해))
//2) DB작업이라면 DAO단 호출
//3) DAO단에서 반환된 값을 Controller단으로 넘겨줌


// 클래스를 구현하기 전 메서드의 집합
public interface HBoardService 
{
	// 브레이스가 없는 메서드 추상메서드 > 구현체(impl)이 필요함
	// 게시판 전체 리스트 조회..하면
	public List<HBoardVO> getAllBoard() throws Exception; // 라는 메서드를 조회한다(impl)~

	// 게시판 글 작성
	public boolean saveBoard(HBoardDTO newBoard) throws Exception;
	
	// 게시판 글 수정
	
	// 게시판 상세 보기
	
	// 게시판 글 삭제
	
	
}
