<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>News</title>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=538f5a0703238f35c77384fc3289d2f7"></script>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Diphylleia&display=swap" rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>

<script>
	const serviceKey = "2BAoAqXIacUdKhVYFlcyX1JlVTO0YYww2AEuq4UkaMnNNV4yUZE%2BWVNvtPjkJS%2FnrTwpHC0QFs45qSKAysaN9A%3D%3D";
	const mapKey = "538f5a0703238f35c77384fc3289d2f7";
	$(function() {
		getTour();
		
	});

	function getTour() {
		$.ajax({
			url : `https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=\${serviceKey}&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=${param.contentid}&contentTypeId=39&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1`,
			type : 'GET',
			dataType : 'json',
			async : false,
			success : function(data) {
				console.log(data);
				output(data.response.body.items.item[0]);
			}
		});
	}
	
	function output(data){
		$('.tourTitle').html(data.title);
		$('.image').html(`<img src='\${data.firstimage}' style='width:500px;height:400px;'/>`)
		showMap(data.mapx, data.mapy, data.mlevel);
	}
	
	function showMap(mapx, mapy, mlevel){
		var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
		var options = { //지도를 생성할 때 필요한 기본 옵션
			center: new kakao.maps.LatLng(mapy, mapx), //지도의 중심좌표.
			level: 3 //지도의 레벨(확대, 축소 정도)
		};

		var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
		
		// 지도를 클릭한 위치에 표출할 마커입니다
		var marker = new kakao.maps.Marker({ 
		    // 지도 중심좌표에 마커를 생성합니다 
		    position: map.getCenter() 
		}); 
		// 지도에 마커를 표시합니다
		marker.setMap(map);
	}
	
</script>
<style>

</style>

</head>


<body>

	<div class="container">
		<c:import url="../header.jsp"></c:import>


		<div class="content">
			<h1 class="tourTitle"></h1>
			<div class="image" ></div>
			<div id="map" style="width:500px;height:400px;"></div>

		</div>

		<c:import url="../footer.jsp"></c:import>

	</div>
</body>
</html>
