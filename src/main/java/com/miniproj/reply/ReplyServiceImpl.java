package com.miniproj.reply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miniproj.model.PagingInfo;
import com.miniproj.model.PagingInfoDTO;
import com.miniproj.model.PointLogDTO;
import com.miniproj.model.ReplyDTO;
import com.miniproj.model.ReplyVO;
import com.miniproj.persistence.MemberDAO;
import com.miniproj.persistence.PointLogDAO;
import com.mysql.cj.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	private final ReplyDAO rDao;
	private final PointLogDAO pDao;
	private final MemberDAO mDao;
	
	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getAllReplies(int boardNo, PagingInfoDTO pagingInfoDTO) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		PagingInfo pi = makePagingInfo(pagingInfoDTO, boardNo);
		List<ReplyVO> list = rDao.getAllReplies(boardNo, pi);
		result.put("pagingInfo", pi);
		result.put("replyList", list);
		
		return result;
	}

	private PagingInfo makePagingInfo(PagingInfoDTO pagingInfoDTO, int boardNo) throws Exception {
		PagingInfo pi = new PagingInfo(pagingInfoDTO);
		
		pi.setTotalPostCnt(rDao.getTotalPostCnt(boardNo)); 
		pi.setTotalPageCnt (); // 전체 페이지 수
		pi.setStartRowIndex(); // 현재 페이지에서 보여주기 시작할 글의 index번호
		//페이징블럭만들기
		pi.setPageBlockNoCurPage();
		pi.setStartPageNoCurBlock();
		pi.setEndPageNoCurBlock();

		return pi;
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public boolean saveReply(ReplyDTO newReply) throws Exception {
		boolean result = false;
		// 댓글 저장 insert
		if (rDao.insertNewReply(newReply)==1) {
			// 포인트 부여 insert
			PointLogDTO pointLogDTO = new PointLogDTO(newReply.getReplyer(), "댓글작성");
			if(pDao.insertPointLog(pointLogDTO) == 1) {
				// 포인트 부여받은 멤버 userPoint update
				if (mDao.updateUserPoint(pointLogDTO) == 1) {
					result = true;
				}
			}
			
		}
		
		return result;
	}

	@Override
	public boolean saveModifyReply(ReplyDTO replyDTO) throws Exception {
		boolean result = false;
		if(rDao.updateReply(replyDTO)==1) {
			result = true;
		}
		return result;
	}

}
