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
				console.log(data);
				outputWeather(data);
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
	
	// 날씨 json데이터 parsing하여 출력
	function outputWeather(data){
		let cityName = data.name;
		$('#cityName').html(cityName);
		
		let dt = new Date(data.dt).toLocaleString();
		$('#outputTime').html("출력시간" + dt);
		
		$('.curWeather').html("날씨 : " + data.weather[0].main);
		$('.weatherIcon').html("<img src='https://openweathermap.org/img/wn/" + data.weather[0].icon + "@2x.png'/>")
		$('.curTemp').html("기온 : " + data.main.temp);
		$('.curDis').html("상세날씨 : " + data.weather[0].description);
		$('.curMaxT').html("최고온도 : " + data.main.temp_max);
		$('.curMinT').html("최저온도 : " + data.main.temp_min);
		$('.curWindS').html("풍속 : " + data.wind.speed);
		$('.curWindD').html("풍향 : " + data.wind.deg);
		let sunrise = new Date(data.sys.sunrise).toLocaleTimeString;
		$('.curRise').html("해 뜨는 시간 : " + sunrise);
		let sunset = new Date(data.sys.sunset);
		$('.curSet').html("해 지는 시간 : " + sunset);
	
	}
	
</script>

</head>
<body>

	<div class="container">
		<c:import url="./header.jsp"></c:import>


		<div class="content">
			<h1>
				<span id="cityName"></span>지역의 오늘의 날씨!
			</h1>
		</div>

		<div id="outputTime"></div>

		<div class="weatherInfo">
			<div class="curWeather"> </div><div class="weatherIcon"> </div>
			<div class="curTemp"> </div>
			<div class="curDis">상세날씨(discription) </div>
			<div class="curMaxT">최고온도 </div>
			<div class="curMinT">최저온도 </div>
			<div class="curWindS">풍속 </div>
			<div class="curWindD">풍향 </div>
			<div class="curRise">해뜨는시간 </div>
			<div class="curSet">해지는시간 </div>
			

		</div>
	
		<c:import url="./footer.jsp"></c:import>
	
	</div>

</body>
</html>