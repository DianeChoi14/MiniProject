<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>News</title>
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=538f5a0703238f35c77384fc3289d2f7"></script>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Diphylleia&display=swap"
	rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>

<script>
	const serviceKey = "2BAoAqXIacUdKhVYFlcyX1JlVTO0YYww2AEuq4UkaMnNNV4yUZE%2BWVNvtPjkJS%2FnrTwpHC0QFs45qSKAysaN9A%3D%3D";
	const mapKey = "538f5a0703238f35c77384fc3289d2f7";
	// 현재 표시되는 슬라이드이미지의 인덱스값
	let slideNo = 0;
	
	$(function() {
		getTour();
		getAdditionalImages();
		/* $('.next').click(function(){
			
			$('.carousel-item').eq(slideNo).removeClass('active');
			$('.carousel-indicators').eq(slideNo).removeClass('active');
			$('.carousel-indicators').eq(slideNo).removeAttr('aria-current');
			
			
			if(slideNo == ($('.carousel-item').length-1)){
				slideNo=0; // 슬라이드의 마지막 사진이미지일 경우 처음이미지 출력
			} else {
				++slideNo;
			}
			$('.carousel-item').eq(slideNo).addClass('active');
			$('.carousel-indicators').eq(slideNo).addClass('active');
			$('.carousel-indicators').eq(slideNo).attr('aria-current', 'true');
		});
		
		$('.prev').click(function(){
			$('.carousel-item').eq(slideNo).removeClass('active');
			$('.carousel-indicators').eq(slideNo).removeClass('active');
			if(slideNo == 0){
				slideNo=$('.carousel-item').length; // 슬라이드의 첫 이미지일 경우 마지막 이미지가 출력되도록
			} 
			$('.carousel-item').eq(--slideNo).addClass('active');
			$('.carousel-indicators').eq(slideNo).addClass('active');
			$('.carousel-indicators').eq(slideNo).removeAttr('aria-current');
		}); */
	});

	function getTour() {
		$
				.ajax({
					url : `https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=\${serviceKey}&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=${param.contentid}&contentTypeId=12&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1`,
					type : 'GET',
					dataType : 'json',
					async : false,
					success : function(data) {
						console.log(data);
						tourOutput(data.response.body.items.item[0]);
					}
				});
	}

	function tourOutput(data) {
		$('.tourTitle').html(data.title);
		$('.image')
				.html(
						`<img src='\${data.firstimage}' style='width:500px;height:400px;'/>`);
		showMap(data.mapx, data.mapy, data.mlevel);
		
	}
	

	function getAdditionalImages() {
		// 이미지슬라이더를 위해 추가 이미지를 가져오는 함수
		$
				.ajax({
					url : `https://apis.data.go.kr/B551011/KorService1/detailImage1?serviceKey=\${serviceKey}&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=${param.contentid}&imageYN=Y&subImageYN=Y&numOfRows=10&pageNo=1`,
					type : 'GET',
					dataType : 'json',
					async : false,
					success : function(data) {
						console.log(data);
						if (parseInt(data.response.body.totalCount) > 0) {
							showImageSlider(data.response.body.items.item);

						} else {
							console.log("추가이미지 없음...")
						}
					}
				});
	}
	
	function showImageSlider(images){

		let output = '';
		let indicatorOutput = '';
		$.each(images, function(i, image){
			if(i==0){
				output += `<div class="carousel-item active">`;
				indicatorOutput +=`<button type="button" data-bs-target="#carousel" data-bs-slide-to="\${i}" class="active"></button>`;
				
			} else {
				output += `<div class="carousel-item">`;
				indicatorOutput +=`<button type="button" data-bs-target="#carousel" data-bs-slide-to="\${i}"></button>`;
			}
			
			output += `<img src="\${image.originimgurl}" alt="\${image.imgname}" class="d-block w-100">`;
			output += `</div>`;
/* 			indicatorOutput +=`<button type="button" data-bs-target="#carousel" data-bs-slide-to="\${i}"></button>`; */

		});
		$('.carousel-inner').html(output);
/* 		$('.carousel-item').eq(slideNo).addClass('active'); */
		$('.carousel-indicators').html(indicatorOutput);
/* 		$('.carousel-indicators button').eq(slideNo).addClass('active');
		$('.carousel-indicators button').eq(slideNo).attr("aria-current", "true"); */
	}

	function showMap(mapx, mapy, mlevel) {
		var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
		var options = { //지도를 생성할 때 필요한 기본 옵션
			center : new kakao.maps.LatLng(mapy, mapx), //지도의 중심좌표.
			level : 3
		//지도의 레벨(확대, 축소 정도)
		};

		var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

		// 지도를 클릭한 위치에 표출할 마커입니다
		var marker = new kakao.maps.Marker({
			// 지도 중심좌표에 마커를 생성합니다 
			position : map.getCenter()
		});
		// 지도에 마커를 표시합니다
		marker.setMap(map);
	}
</script>
<style>
.slide {
	display : flex;
	align-items : center;
	width : 500px;
	height : 400 px;
	
}
</style>

</head>


<body>

	<div class="container">
		<c:import url="../header.jsp"></c:import>

		<div class="content">

			<!-- Carousel -->
			<div id="carousel" class="carousel slide" data-bs-ride="carousel">

				<!-- Indicators/dots -->
				<div class="carousel-indicators"></div>

				<!-- The slideshow/carousel -->
				<div class="carousel-inner">
					
					
				</div>

				<!-- Left and right controls/icons -->
				<button class="carousel-control-prev prev" type="button"
					data-bs-target="#carousel" data-bs-slide="prev">
					<span class="carousel-control-prev-icon"></span>
				</button>
				<button class="carousel-control-next next" type="button"
					data-bs-target="#carousel" data-bs-slide="next">
					<span class="carousel-control-next-icon"></span>
				</button>
			</div>

			<h1 class="tourTitle"></h1>
			<div class="image"></div>
			<div id="map" style="width: 500px; height: 400px;"></div>

		</div>

		<c:import url="../footer.jsp"></c:import>

	</div>
</body>
</html>
