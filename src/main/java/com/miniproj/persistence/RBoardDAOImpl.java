package com.miniproj.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.HReplyBoardDTO;
import com.miniproj.model.PagingInfo;
import com.miniproj.model.SearchCriteriaDTO;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class RBoardDAOImpl implements RBoardDAO {
	
	private static final String NS = "com.miniproj.mappers.rboardmapper";
	private final SqlSession ses ;

	@Override
	public List<HBoardVO> selectAllBoard(PagingInfo pi) throws Exception {
		System.out.println("!");
		List<HBoardVO> lst = ses.selectList(NS + ".getAllHBoard", pi);
		for(HBoardVO h : lst)
		{
			System.out.println(h.toString());
		}
		
		return ses.selectList(NS + ".getAllHBoard", pi);
	}

	@Override
	public int insertNewBoard(HBoardDTO newBoard) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxBoardNo() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertBoardUpFile(BoardUpFilesVODTO upFile) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BoardDetailInfo> selectBoardByBoardNo(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateReadCount(int boardNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectDateDiff(int boardNo, String ipAddr) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int saveBoardReadLog(int boardNo, String ipAddr) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateReadWhen(int boardNo, String ipAddr) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateBoardRef(int newBoardNo) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertReplyBoard(HReplyBoardDTO replyBoard) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateBoardRef(int ref, int refOrder) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<BoardUpFilesVODTO> selectBoardUpFiles(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllBoardUpFiles(int boardNo) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public int deleteBoardByBoardNo(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateBoardbyBoardNo(HBoardDTO modifyBoard) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteBoardUpFiles(int boardUpFileNo) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<HBoardVO> selectPopBoards() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalPostCnt() throws Exception {
		// TODO Auto-generated method stub
		return ses.selectOne(NS + ".selectTotalCnt");
	}

	@Override
	public int getTotalPostCnt(SearchCriteriaDTO sc) throws Exception {
		// TODO Auto-generated method stub
		return ses.selectOne(NS + ".selectTotatlCntWithSearchCriteria", sc);
	}

	@Override
	public List<HBoardVO> selectAllBoard(PagingInfo pi, SearchCriteriaDTO sc) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("searchType", sc.getSearchType());
		params.put("searchWord", "%" + sc.getSearchWord() + "%");
		params.put("startRowIndex", pi.getStartRowIndex());
		params.put("viewPostCntPerPage", pi.getViewPostCntPerPage());
		
		return ses.selectList(NS + ".getSearchBoardWithPaging", params);
	}

}
