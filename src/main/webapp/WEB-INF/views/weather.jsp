<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오늘의날씨(연습)</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
	let apiKey = '2043554dd99d8736e87dc0facd709ba8';
	let baseUrl = 'https://api.openweathermap.org/data/2.5/weather?appid='+ apiKey + '&q=seoul&units=metric';

	$(function(){
		getWeatherData();	
	});

	function getWeatherData(){
		// 자바스크립트에서는 {}를 객체로 받는다
		$.ajax({
			url : baseUrl, // 데이터가 송수신될 서버의 주소
			type : 'get', // 통신방식 get, post, put, delete, patch
			dataType : 'json', // 수신받을 데이터의 타입 text, xml, json
			success : function(data){	// 익명함수anonymous
				// 비동기 통신에 성공하면 자동으로 호출될 callback function
				console.log(data)
			},
			error : function(){
				// 비동기 통신에 실패하면 자동으로 호출될 callback function
			},
			complete : function(){
				// 통신성공/실패 여부에 상관없이 마지막에 호출될 callback function
			}
			// 쿼리스트링으로 전달하는 정보밖에 없으므로 data생략
		})
	}
</script>

</head>
<body>

	<div class="container">
		<c:import url="./header.jsp"></c:import>

	
	<div class="content">
		<h1>오늘의 날씨!</h1>
	</div>
	<div class="weatherInfo">


	</div>
	<c:import url="./footer.jsp"></c:import>
	
	</div>

</body>
</html>