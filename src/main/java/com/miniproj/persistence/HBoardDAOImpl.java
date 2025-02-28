package com.miniproj.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;
import org.springframework.stereotype.Repository;

import com.miniproj.model.BoardDetailInfo;
import com.miniproj.model.BoardUpFilesVODTO;
import com.miniproj.model.HBoardDTO;
import com.miniproj.model.HBoardVO;
import com.miniproj.model.HReplyBoardDTO;
import com.miniproj.model.PagingInfo;
import com.miniproj.model.SearchCriteriaDTO;
import com.miniproj.model.SeoulTempVO;

@Repository // 아래의 클래스가 dao객체임을 명시
public class HBoardDAOImpl implements HBoardDAO 
{
	@Autowired
	private SqlSession ses;
	
	private static String NS = "com.miniproj.mappers.hboardmapper";
	
	
	// Exception 예외처리의 부모 클래스
	@Override
	public List<HBoardVO> selectAllBoard(PagingInfo pi) throws Exception // throws : 현재 메서드에서 예외가 발생하면 현재 메서드를 호출한 곳에서 예외처리를 하도록 미뤄두는 키워드
	{
		System.out.println("Here is HBoard DAO..............");
		
		List<HBoardVO> list;
	
			list = ses.selectList(NS + ".getAllHBoard", pi);
			
//		for (HBoardVO b : list)
//		{
//			System.out.println(b.toString());
//		}
		return list;
	}


	@Override
	public int insertNewBoard(HBoardDTO newBoard) 
	{
		return ses.insert(NS + ".saveNewBoard", newBoard);
	}


	@Override
	public int getMaxBoardNo() throws Exception {
		
		return ses.selectOne(NS+".getMaxNo");
	}


	@Override
	public int insertBoardUpFile(BoardUpFilesVODTO upFile) throws Exception {

		return ses.insert(NS + ".saveUpFile", upFile);
		
	}


	@Override
	public List<BoardDetailInfo> selectBoardByBoardNo(int boardNo) throws Exception {

		return ses.selectList(NS + ".selectBoardDetailInfoByBoardNo" , boardNo);
	}


	@Override
	public int updateReadCount(int boardNo) {
		return ses.update(NS+".updateReadCount", boardNo);
		
	}


	@Override
	public int selectDateDiff(int boardNo, String ipAddr) throws Exception {
		// SqlSessoin템플릿의 메서드는 파라메터를 하나만 받을 수 있는데, 
		// 넘겨줘야할 파라메터가 2개 이상이면 Map을 이용하여 Map을 사용하여 파라메터를 매핑하여 넘긴다.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("readWho", ipAddr);
		params.put("boardNo", boardNo);
		
		return ses.selectOne(NS + ".selectBoardDateDiff", params);
	}


	@Override
	public int saveBoardReadLog(int boardNo, String ipAddr) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("readWho", ipAddr);
		params.put("boardNo", boardNo);
		
		return ses.insert(NS + ".saveBoardReadLog", params);
	}


	@Override
	public int updateReadWhen(int boardNo, String ipAddr) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("readWho", ipAddr);
		params.put("boardNo", boardNo);
		
		return ses.update(NS + ".updateBoardReadLog", params);
	}


	@Override
	public int updateBoardRef(int newBoardNo) throws Exception {
		return ses.update(NS + ".updateBoardRef", newBoardNo);
	}


	@Override
	public int insertReplyBoard(HReplyBoardDTO replyBoard) throws Exception {
		
		return ses.insert(NS + ".insertReplyBoard", replyBoard);
	}


	@Override
	public void updateBoardRef(int ref, int refOrder) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ref", ref);
		params.put("refOrder", refOrder);
		
		ses.update(NS + ".updateBoardREfOrder", params);
	}


	@Override
	public List<BoardUpFilesVODTO> selectBoardUpFiles(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		return ses.selectList(NS + ".selectBoardUpFiles", boardNo);
	}


	@Override
	public void deleteAllBoardUpFiles(int boardNo) throws Exception {
		ses.delete(NS + ".deleteAllBoardFiles" , boardNo);
		
	}


	@Override
	public int deleteBoardByBoardNo(int boardNo) throws Exception {

		return ses.update(NS + ".deleteBoardByBoardNo", boardNo);
		
	}


	@Override
	public int updateBoardbyBoardNo(HBoardDTO modifyBoard) throws Exception {

		return  ses.update(NS + ".updateBoardByBoardNo", modifyBoard);
	}


	@Override
	public void deleteBoardUpFiles(int boardUpFileNo) {
		ses.delete(NS + ".deleteBoardUpFileByPK", boardUpFileNo);
	}


	@Override
	public List<HBoardVO> selectPopBoards() throws Exception {
		return ses.selectList(NS + ".selectPopBoards");
	}


	@Override
	public int getTotalPostCnt() throws Exception {

		return ses.selectOne(NS + ".selectTotalCnt");
	}


	@Override
	public int getTotalPostCnt(SearchCriteriaDTO sc) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("searchType", sc.getSearchType());
		params.put("searchWord", "%" + sc.getSearchWord() + "%");
	
		return ses.selectOne(NS + ".selectTotatlCntWithSearchCriteria", params);
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


	@Override
	public List<SeoulTempVO> getSeoulTemp() throws Exception {
		
		return ses.selectList(NS + ".getSeoulTemp");
	}
	
	
	
}
