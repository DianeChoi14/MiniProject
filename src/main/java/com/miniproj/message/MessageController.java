package com.miniproj.message;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniproj.model.FriendVO;
import com.miniproj.model.MyResponseWithData;
import com.miniproj.model.ResponseType;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
	
	private final MessageService msgService;
	
	@GetMapping(value="/getFriends/{userId}")
	public ResponseEntity getReceiveUsers(@PathVariable("userId")String userId) {
		System.out.println(userId + "의 친구목록을 불러올게~");
		
		ResponseEntity result = null;
		MyResponseWithData<List<FriendVO>> fList = null;
		try {
			fList = new MyResponseWithData<List<FriendVO>> (ResponseType.SUCCESS, msgService.getFriends(userId));
			result = new ResponseEntity(fList, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = new ResponseEntity(MyResponseWithData.fail(), HttpStatus.BAD_REQUEST);
		}
		return result;
	}
}
