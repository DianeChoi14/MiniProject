<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<meta charset="UTF-8">
<title>INDEX</title>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=538f5a0703238f35c77384fc3289d2f7"></script>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Diphylleia&display=swap" rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
</head>
<script>
google.charts.load('current', {'packages':['line']});
// google.charts.setOnLoadCallback(drawChart);

	$(function(){
		getSeoulTemp();
	});
	
	function getSeoulTemp(){
		$.ajax({
			url : '/seoulTemp', // 데이터가 송수신될 서버의 주소
			type : 'GET', // 통신 방식 : GET, POST, PUT, DELETE, PATCH   
			dataType : 'json', // 수신 받을 데이터의 타입 (text, xml, json)
			success : function(data) { // 비동기 통신에 성공하면 자동으로 호출될 callback function
				console.log(data);
				if(data.resultCode == 200) {
					dataToArray(data.data);
				}
			}
		});
	}
	
	// 데이터([{}], 배열 안에 객체 형태)를 받아서 2차원 배열로 만들기
	function dataToArray(data) {
		let seoulTemps=[];
		seoulTemps[0]=["날짜", "평균기온", "최저기온", "최고기온"]; // 범례
		$.each(data, function(i, row){
			let tempDate=new Date(row.writtenDate).toDateString();
			let avgTemp = row.avgTemp;
			let minTemp = row.minTemp;
			let maxTemp = row.maxTemp;

			seoulTemps[i] = [tempDate, avgTemp, minTemp, maxTemp];
			
		});
		drawChart(seoulTemps);
	}
	
	function drawChart(seoulTemps) {

	      var data = new google.visualization.DataTable();
	      data.addColumn('string', '날짜');
	      data.addColumn('number', '평균기온');
	      data.addColumn('number', '최저기온');
	      data.addColumn('number', '최고기온');

	      data.addRows(seoulTemps);

	      var options = {
	        chart: {
	          title: '2023 서울 기온 차트'
	        },
	        width: 900,
	        height: 500
	      };

	      var chart = new google.charts.Line(document.getElementById('linechart_material'));

	      chart.draw(data, google.charts.Line.convertOptions(options));
	    }
</script>

<style>
* {
	font-family: "Orbit", sans-serif; 
	font-weight: 800;
	font-style: normal ;
}
</style>

<body>
   <div class="container">
      <jsp:include page="./header.jsp"></jsp:include>

      <div class="content">
      <h1>Seoul Temp 2023</h1>
      <div id="linechart_material"></div>
      </div>
      <jsp:include page="./footer.jsp"></jsp:include>
      <script src="https://kit.fontawesome.com/828eac3ad2.js" crossorigin="anonymous"></script>
   </div>
</body>
</html>
