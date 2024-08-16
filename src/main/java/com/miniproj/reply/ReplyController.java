package com.miniproj.reply;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniproj.model.MyResponseWithData;
import com.miniproj.model.PagingInfoDTO;
import com.miniproj.model.ReplyVO;
import com.miniproj.model.ResponseType;
import com.miniproj.service.hboard.RBoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {
	
	private final ReplyService rService;
	
	@GetMapping("/all/{boardNo}/{pageNo}") // GET방식일 때 데이터를 얻어옴, {} : path variable
 	public ResponseEntity getAllReplyByBoardNo(@PathVariable("boardNo") int boardNo, @PathVariable("pageNo") int pageNo) {
		System.out.println(boardNo + "번의 모든 댓글을 얻어오자!");
		
		ResponseEntity result = null;
		Map<String, Object> replies = null;
		
		try {
			replies = rService.getAllReplies(boardNo, new PagingInfoDTO(pageNo, 5));
			result = new ResponseEntity(MyResponseWithData.success(replies), HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			result = new ResponseEntity (MyResponseWithData.fail(), HttpStatus.BAD_REQUEST);
		}
		return result;
	}
}
