package com.miniproj.model;

import lombok.Getter;
import lombok.ToString;

// view에서 페이징처리를 하기 위한 모든 정보를 담고 있는 객체
// 세터값을 따로 주기 위해서 롬복으로 자동생성자를 만들지 않음
@Getter
@ToString
public class PagingInfo {
	//=============================기본 페이징에 필요한 변수들===========================
	private int pageNo; // 현재 페이지 번호
	private int viewPostCntPerPage; // 1페이지당 보여줄 글의 갯수	
	private int totalPostCnt; // 전체 글 갯수
	private int totalPageCnt; // 전체 >페이지< 수
	private int startRowIndex; // 현재 페이지에서 보여주기 시작할 글의 index번호	
	//=====================================================================================
	//===============================페이징블럭을 만들 때 필요한 변수들====================
	private int pageCntPerBlock = 10; // 한 개의 페이징 블럭에 몇 페이지를 보여줄 것인가
	private int pageBlockNoCurPage; // 현재 페이지가 속한 페이징 블럭 번호 	
	private int startPageNoCurBlock; // 연제 페이징 블럭에서 출력시작할 페이지 번호
	private int endPageNoCurBlock; // 현제 페이징블럭에서 출력 끝 페이지 번호

	//=====================================================================================
	
	public PagingInfo(PagingInfoDTO dto) {
		this.pageNo = dto.getPageNo();
		this.viewPostCntPerPage = dto.getPagingSize();
	}
	
	public void setTotalPostCnt (int totalPostCnt) {
		this.totalPostCnt = totalPostCnt;
	}
	public void setTotalPageCnt () {
		if((this.totalPostCnt % this.viewPostCntPerPage) == 0) {
			this.totalPageCnt = (this.totalPostCnt / this.viewPostCntPerPage) ;
		} else {
			this.totalPageCnt = (this.totalPostCnt / this.viewPostCntPerPage) +1 ;
		}
			
	}
	
	public void setStartRowIndex() {
		this.startRowIndex = (this.pageNo-1) * this.viewPostCntPerPage;
	}
	
	public void setPageBlockNoCurPage() {
		if(this.pageNo % this.pageCntPerBlock == 0) {
			this.pageBlockNoCurPage = this.pageNo / this.pageCntPerBlock;
		} else {
			this.pageBlockNoCurPage = this.pageNo / this.pageCntPerBlock + 1;
		}
		
	}
	
	public void setStartPageNoCurBlock() {
		//(현재 페이징 블럭번호 -1) * 1개 페이징 블럭에서 보여줄 페이지 수 + 1
		this.startPageNoCurBlock = (this.pageBlockNoCurPage -1) * this.pageCntPerBlock +1;
	}
	
	public void setEndPageNoCurBlock() {
		//1)에서 나온 값 + (1개 페이징 블럭에서 보여줄 페이지 수 - 1)
		this.endPageNoCurBlock = this.startPageNoCurBlock + (this.pageCntPerBlock - 1);
		if(this.totalPageCnt < this.endPageNoCurBlock) {
			// 데이터가 나오지 않는 페이지 지우기
			this.endPageNoCurBlock = this.totalPageCnt;
		} 
	}
}
