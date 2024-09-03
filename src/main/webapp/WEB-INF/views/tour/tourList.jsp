<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>News</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
   href="https://fonts.googleapis.com/css2?family=Nanum+Pen+Script&display=swap"
   rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<script>
	const serviceKey="2BAoAqXIacUdKhVYFlcyX1JlVTO0YYww2AEuq4UkaMnNNV4yUZE%2BWVNvtPjkJS%2FnrTwpHC0QFs45qSKAysaN9A%3D%3D";
	
	$(function(){
		getTourList();
	});
	
	function getTourList() {
		$.ajax({
			url : `https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=\${serviceKey}&numOfRows=20&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&contentTypeId=39&areaCode=1`, 
			type : 'GET', 
			dataType : 'json', 
			success : function(data) { 
				console.log(data);
				outputTours(data);
			}
		});
	}
	function outputTours(data){
		let items = data.response.body.items.item;
		let output = '';

		$.each(items, function(i, tour){
			output += `<div class="tour" onclick="location.href='/tourSub?contentid=\${tour.contentid}'"><div class="tourThumb">`;
			if(tour.firstimage2 ==null || tour.firstimage2 == '') {
				output += `<img src="/resources/images/noimage.png" style="width:190px; height:190px;"/>`;
				
			} else {
				output += `<img src="\${tour.firstimage2}" style="width:190px; height:190px;"/>`;
			}
			
			output += `</div>`;
			output += `<div class="tourText"><div class="tourTitle">\${tour.title}</div>`;
			output += `<div class="tourDescription">\${tour.addr1}</div>`;
			output += `</div></div>`;
			
		});
		$('.tourList').html(output);
	}

</script>
<style>
.tourList{
}
.tour{
	width : 240px;
	border : 1px solid #ccc;
	margin : 20px 15px;
	padding: 10px;
	text-align: center;
	float: left;
}
.tour:hover{
	border : 3px solid #777;
}
.tourThumb img {
	width : 100%;
}
.tourDescription{
	font-size:0.7em;
}
.tourText{
	padding-top: 10px;
}
.myfooter{
	clear:left;
}
</style>

</head>


<body>

	<div class="container">
		<c:import url="../header.jsp"></c:import>


		<div class="content">
			<h1>위치기반 관광정보</h1>
			
		<div class="tourList">
					
			
		</div>	

		</div>

		<c:import url="../footer.jsp"></c:import>

	</div>
</body>
</html>
