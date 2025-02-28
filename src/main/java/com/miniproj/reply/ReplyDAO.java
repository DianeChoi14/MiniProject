package com.miniproj.reply;

import java.util.List;

import com.miniproj.model.PagingInfo;
import com.miniproj.model.ReplyDTO;
import com.miniproj.model.ReplyVO;

public interface ReplyDAO {
	List<ReplyVO> getAllReplies(int boardNo, PagingInfo pi) throws Exception;

	int getTotalPostCnt(int boardNo) throws Exception;
	
	int insertNewReply(ReplyDTO newReplyDTO)throws Exception;

	int updateReply(ReplyDTO replyDTO) throws Exception;
}
