package com.miniproj.reply;

import java.util.List;
import java.util.Map;

import com.miniproj.model.PagingInfoDTO;
import com.miniproj.model.ReplyDTO;
import com.miniproj.model.ReplyVO;

public interface ReplyService {
	Map<String, Object> getAllReplies(int boardNo, PagingInfoDTO pagingInfoDTO) throws Exception;

	boolean saveReply(ReplyDTO newReply) throws Exception;

	boolean saveModifyReply(ReplyDTO replyDTO) throws Exception;
}
