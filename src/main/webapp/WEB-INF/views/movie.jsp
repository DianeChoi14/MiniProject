<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>일별 박스오피스</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
	let apiKey = '141d3124fbc0d431971daa2035ed8e59';
	let baseUrl = 'http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=' + apiKey +'&targetDt=20240712'

	$(function(){
		getTodayMovie();
	}); // InnerFunction > 외부의 함수는 함수로 감싸져있는 내부함수를 불러들일 수 없지만 내부에서는 외부를 불러들일 수 있다
	
	function getTodayMovie(){
		$.ajax({
			url:baseUrl,
			type:'get',
			dataType:'json',
			success:function(data){
				movieData(data);
			}
		})
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
		
		
    <a href="#" class="list-group-item list-group-item-action">First item</a>
    <a href="#" class="list-group-item list-group-item-action">Second item</a>
    <a href="#" class="list-group-item list-group-item-action">Third item</a>
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
	
	</div>
</body>
</html>