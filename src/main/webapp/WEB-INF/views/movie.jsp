<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>일별 박스오피스</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
	let now = new Date(); // 현재 날짜와 시간
	now.setDate(now.getDate()-1);
	now = now.toLocaleDateString(); // 2024.7.14. > .을 기준으로 숫자를 나눠서 자릿수에 맞춘 값으로 변환 e.g.20240714
	//let targetDt = 박스오피스 기준일을 현재날짜-1 일을 가져오게 함
	let targetDt = toTargetDt(now);

	let apiKey = '141d3124fbc0d431971daa2035ed8e59';
	let baseUrl = 'http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=' + apiKey +'&targetDt='+ targetDt;
		
	$(function(){
		getTodayMovie(targetDt);
		$('#selectDate').change(function(){
			//alert('선택한 태그의 값이 변경됨 :' + $(this).val());
			let selectDate = new Date($(this).val());
			let targetDt =  toTargetDt(selectDate.toLocaleDateString());
			console.log(targetDt);
			getTodayMovie(targetDt);
		});
	}); // InnerFunction > 외부의 함수는 함수로 감싸져있는 내부함수를 불러들일 수 없지만 내부에서는 외부를 불러들일 수 있다
	
	function toTargetDt(now){
		let dateArr = now.split('.');
		let targetDt = '';
		for(let i=0; i<3 ; i++) {
			targetDt = dateArr[0].trim();
			if (parseInt(dateArr[1]) < 10){
				targetDt += '0' + dateArr[1].trim();	
			} else {
				targetDt += dateArr[1].trim();
			}
			if (parseInt(dateArr[2]) < 10){
				targetDt += '0' + dateArr[2].trim();	
			} else {
				targetDt += dateArr[2].trim();	
			}
			
			console.log("변환된 값 : "+ targetDt);
			return targetDt;
		}
	}
	
	function getTodayMovie(targetDt){
		let apiKey = '141d3124fbc0d431971daa2035ed8e59';
		let baseUrl = 'http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=' + apiKey +'&targetDt='+ targetDt;
		$.ajax({
			url:baseUrl,
			type:'get',
			dataType:'json',
			success:function(data){
				movieData(data);
			}
		});
	}
	
	function movieData(data){
		$('.showRange').html("기준일 : " + data.boxOfficeResult.showRange);
		$('.boxofficeType').html("박스오피스 타입 : " + data.boxOfficeResult.boxofficeType);
		$('.rank').html("순위 : " + data.boxOfficeResult.dailyBoxOfficeList[0].showRange);
		$('.movieNm').html("영화명 : " + data.boxOfficeResult.dailyBoxOfficeList[0].movieNm);
		$('.openDt').html("개봉일 : " + data.boxOfficeResult.dailyBoxOfficeList[0].openDt);
		$('.salesAmt').html("판매량 : " + data.boxOfficeResult.dailyBoxOfficeList[0].salesAmt);
		$('.audiCnt').html("관객수 : " + data.boxOfficeResult.dailyBoxOfficeList[0].audiCnt);
		
		let output =  '<div class="list-group">';
		$.each(data.boxOfficeResult.dailyBoxOfficeList, function(i, e){
			console.log(e);
			output += ' <a href="#" class="list-group-item list-group-item-action">' + e.movieNm + '</a>';
		});
		output +="</div>";
		$('.boxOffice').html(output);
		
	}
	</script>

</head>
<body>
	<div class="container">
		<c:import url="./header.jsp"></c:import>


		<div class="boxOffice">
			<h1>일별 박스오피스! 1위!!</h1>
			<div style="margin-top: 15px; margin-bottom: 15px;">
				<input type="date" class="form-control" id="selectDate">
				<button type="button" class="btn btn-success">Success</button>
			</div>
		</div>
	</div>
	<div class="showRange"></div>
	<div class="boxofficeType"></div>
	<div class="rank"></div>
	<div class="movieNm"></div>
	<div class="openDt"></div>
	<div class="salesAmt"></div>
	<div class="audiCnt"></div>

	<c:import url="./footer.jsp"></c:import>

	
</body>
</html>