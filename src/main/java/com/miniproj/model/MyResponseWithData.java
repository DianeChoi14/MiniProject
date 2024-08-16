package com.miniproj.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//@Builder @AllArgsConstructor // 클래스 위에 사용하는 builder 어노테이션은 아래 생성자가 가지고 있는 모든 멤버 변수를 builder패턴으로 만들어준다
/**
 * @작성자 : 802-05
 * @작성일 : 2024. 8. 16.
 * @프로젝트명 : MiniProject
 * @패키지명 : com.miniproj.model
 * @파일명 : MyResponseWithData.java
 * @클래스명 : MyResponseWithData
 * @description : 
 */
@Getter // 멤버변수를 제이슨으로 바꿔주므로 없으면 에러가 난다
public class MyResponseWithData<T> {
	private int resultCode;
	private String resultMsg;
	private T data; // 제너릭 타입 : 실행될 때 객체의 타입이 결정됨
	
	@Builder // 생성자 위에 사용하는 builder 어노테이션은 아래 생성자가 가지고 있는 변수를 builder패턴으로 만들어준다
	public MyResponseWithData(ResponseType responseType, T data) {
		this.resultCode = responseType.getResultCode();
		this.resultMsg = responseType.getResultMsg();
		this.data = data;
	}
	
	
	/**
	 * @작성자 : 802-05
	 * @작성일 : 2024. 8. 16.
	 * @method_name : success
	 * @param :
	 * @return : MyResponseWithData 
	 * @throws : 
	 * @description : JSON데이터없이 성공했다는 코드(200)와 "success"만 전송할 때 사용
	*/
	public static MyResponseWithData success() {
		
		return MyResponseWithData.builder()
				.responseType(ResponseType.SUCCESS)
				.build();
	}
	
	// 제너릭타입을 사용하는 메서드라는 의미에서 앞에 <T>를 붙인다
	/**
	 * @작성자 : 802-05
	 * @작성일 : 2024. 8. 16.
	 * @method_name : success
	 * @param : 제너릭타입의 json으로 만들어줄 data
	 * @return : MyResponseWithData<T> 
	 * @throws : 
	 * @description : data + 성공했다는 코드(200)와 "success"를 전송할 때 사용
	*/
	public static <T> MyResponseWithData<T> success(T data) {
		return new MyResponseWithData<> (ResponseType.SUCCESS, data);
	}
	
	/**
	 * @작성자 : 802-05
	 * @작성일 : 2024. 8. 16.
	 * @method_name : fail
	 * @return : MyResponseWithData 
	 * @throws : 
	 * @description : 실패 코드(400)와 "fail"전송할 때 사용
	*/
	public static MyResponseWithData fail() {
		return MyResponseWithData.builder()
				.responseType(ResponseType.FAIL)
				.build();
	}
}
