package com.miniproj.reply;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.miniproj.model.MyResponseWithData;
import com.miniproj.model.PagingInfoDTO;
import com.miniproj.model.ReplyDTO;
import com.miniproj.model.ReplyVO;
import com.miniproj.model.ResponseType;
import com.miniproj.service.hboard.RBoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {
	
	private final ReplyService rService;
	
	// produces = {"application/json; charset=UTF-8;"}) : 반환값은 json타입이고, utf-8로 인코딩되어있다 라는 메타정보
	@GetMapping(value="/all/{boardNo}/{pageNo}", produces = {"application/json; charset=UTF-8;"}) // GET방식일 때 데이터를 얻어옴, {} : path variable
 	public ResponseEntity getAllReplyByBoardNo(@PathVariable("boardNo") int boardNo, @PathVariable("pageNo") int pageNo) {
		System.out.println(boardNo + "번의 모든 댓글을 얻어오자!( 댓글 페이징 : " + pageNo +" )");
		
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
	
	@PostMapping(value="/{boardNo}", produces = {"application/json; charset=UTF-8;"}) //@PostMapping = @RequestMapping(method=RequestMethod.POST)
	public ResponseEntity saveReply(@RequestBody ReplyDTO newReply, @PathVariable("boardNo") int boardNo) { // @RequestBody : saveReply에서 넘어오는 데이터가 json데이터임을 알려줌
		System.out.println(boardNo + "번 게시글에 새로운 댓글 : " + newReply.toString());
		ResponseEntity result = null;
		
		try {
			if(rService.saveReply(newReply)) {
				result = new ResponseEntity(MyResponseWithData.success(), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new ResponseEntity(MyResponseWithData.fail(), HttpStatus.BAD_REQUEST);
		}
		return result;
	}
}
