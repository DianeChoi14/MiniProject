package com.miniproj.model;

public enum ResponseType {
	// 텍스트로 되어있어도 스트링타입이 아니고, 자동으로 내부적으로 값의 순서대로 int값을 갖는다
	SUCCESS(200), FAIL(400); // 입력값은 자동으로 name이란 메서드로 작동하게 하여 String으로 반환된다, 괄호 안의 내용은 값이 된다.
	
	private int resultCode;
	
	// enum타입의 생성자는 private만 가능하다 > 접근제어자가 생략되면 private이다..
	ResponseType (int resultCode) {
		this.resultCode = resultCode;
	}
	
	public int getResultCode() {
		return this.resultCode; // MyResponseWithData의 resultCode에 반환
	}
	
	public String getResultMsg() {
		return this.name(); // SUCCESS, FAIL 이 String으로 반환
	}
}
