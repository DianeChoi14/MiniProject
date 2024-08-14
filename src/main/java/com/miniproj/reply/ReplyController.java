package com.miniproj.reply;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.miniproj.model.ReplyVO;
import com.miniproj.service.hboard.RBoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {
	
	private final ReplyService rService;
	
	@GetMapping("/all/{boardNo}") // GET방식일 때 데이터를 얻어옴, {} : path variable
 	public @ResponseBody List<ReplyVO> getAllReplyByBoardNo(@PathVariable("boardNo") int boardNo) {
		System.out.println(boardNo + "번의 모든 댓글을 얻어오자!");
		List<ReplyVO> result = null;
	
		try {
			result = rService.getAllReplies(boardNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
