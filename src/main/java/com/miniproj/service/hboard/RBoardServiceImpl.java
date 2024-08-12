package com.miniproj.service.hboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.HReplyBoardDTO;
import com.miniproj.model.PagingInfo;
import com.miniproj.model.PagingInfoDTO;
import com.miniproj.model.PointLogDTO;
import com.miniproj.model.SearchCriteriaDTO;
import com.miniproj.persistence.MemberDAO;
import com.miniproj.persistence.PointLogDAO;
import com.miniproj.persistence.RBoardDAO;
import com.mysql.cj.util.StringUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RBoardServiceImpl implements RBoardService {

	private final RBoardDAO rDao; 
	private final PointLogDAO pDao;
	private final MemberDAO mDao;
	
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getAllBoard(PagingInfoDTO dto, SearchCriteriaDTO searchCriteria) throws Exception {

		// System.out.println("!service");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 페이징정보
		PagingInfo pi = makePagingInfo(dto, searchCriteria);

		// DAO단 호출
		List<HBoardVO> lst = null;
		if(StringUtils.isNullOrEmpty(searchCriteria.getSearchType()) && StringUtils.isNullOrEmpty(searchCriteria.getSearchWord())) {
			lst = rDao.selectAllBoard(pi);  // 검색어가 없을 때..
		} else {
			lst = rDao.selectAllBoard(pi, searchCriteria);
		}
		
		resultMap.put("pagingInfo", pi);
		resultMap.put("boardList", lst);
		return resultMap;
	}
	
	private PagingInfo makePagingInfo(PagingInfoDTO dto, SearchCriteriaDTO sc) throws Exception {
		PagingInfo pi = new PagingInfo(dto);
		
		// 검색어가 없을 때는 전체 데이터 수를 얻어오는 것 부터 페이징 시작
		// 검색어가 있을 때는 검색한 글의 데이터 수를 얻어오는 것부터 페이징 시작
		if(StringUtils.isNullOrEmpty(sc.getSearchType()) && StringUtils.isNullOrEmpty(sc.getSearchWord())) {
			// 전체 게시글 수 세팅(검색어 없을 때)
			pi.setTotalPostCnt(rDao.getTotalPostCnt()); 
		} else {
			// System.out.println("검색결과수 :" + rDao.getTotalPostCnt(sc));
			pi.setTotalPostCnt(rDao.getTotalPostCnt(sc)); // 검색조건에 따라 데이터 세팅
		}
		
		pi.setTotalPageCnt (); // 전체 페이지 수
		pi.setStartRowIndex(); // 현재 페이지에서 보여주기 시작할 글의 index번호
		//페이징블럭만들기
		pi.setPageBlockNoCurPage();
		pi.setStartPageNoCurBlock();
		pi.setEndPageNoCurBlock();

		// System.out.println(pi.toString() + "페이징 처리되었다!!!!");
		return pi;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public boolean saveBoard(HBoardDTO newBoard) throws Exception {
		
		boolean result = false;
//		1. newBoard를 DAO단을 통해 insert해본다 
		if (rDao.insertNewBoard(newBoard) == 1) {
//			1. 글작성자의 포인트를 부여한다. (select+insert)
			PointLogDTO pointLogDTO = new PointLogDTO(newBoard.getWriter(), "글작성");
			if (pDao.insertPointLog(pointLogDTO) == 1) {
//				3. 작성자의 userPoint값을 update
				if (mDao.updateUserPoint(pointLogDTO) == 1) {
					result = true;
				}
			}
		}

		return result;
	}

	@Override
	public boolean modifyBoard(HBoardDTO modifyBoard) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BoardDetailInfo read(int boardNo, String ipAddr) throws Exception {
		BoardDetailInfo boardInfo = rDao.selectBoardByBoardNo(boardNo);
		System.out.println("댓글형게시판 상세보기 서비스단!");
		// 조회수 증가 > 글을 읽어오기 전에 조회수를 가져와서 글을 가져왔을 경우 조회수를 1 늘린다.
		if (boardInfo != null) {
			// ipAddr유저가 boardNo글을 조회한 적이 없다.
			if (rDao.selectDateDiff(boardNo, ipAddr) == -1) {
				if (rDao.saveBoardReadLog(boardNo, ipAddr) == 1) { // 조회내역 저장
					//updateReadCount(boardNo, boardInfo);
				}
			} else if (rDao.selectDateDiff(boardNo, ipAddr) >= 1) {
				//updateReadCount(boardNo, boardInfo);
				rDao.updateReadWhen(boardNo, ipAddr);
			}

		}
		return boardInfo;

	}

	@Override
	public List<BoardDetailInfo> read(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BoardUpFilesVODTO> removeBoard(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveReply(HReplyBoardDTO replyBoard) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<HBoardVO> getPopularBoards() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
